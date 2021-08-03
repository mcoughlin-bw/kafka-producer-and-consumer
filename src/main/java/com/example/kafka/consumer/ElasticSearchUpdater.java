package com.example.kafka.consumer;

import com.example.kafka.model.Call;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ElasticSearchUpdater {

    @Value("${call.es.index}")
    private String indexName;
    private final RestHighLevelClient client;

    /**
     * Sends a batch of records to ElasticSearch
     * Returns a set of failed ES record ids
     *
     * @param batch
     * @return
     * @throws IOException
     */
    public Set<String> indexBatch(List<Call> batch) {
        BulkRequest bulkRequest = new BulkRequest(indexName);

        bulkRequest.add(batch.stream()
                .map(this::createUpdateRequest)
                .collect(Collectors.toList()));

        BulkResponse bulkResponse = null;
        try {
            bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bulkResponse == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(bulkResponse.getItems())
                .filter(BulkItemResponse::isFailed)
                .map(BulkItemResponse::getId)
                .collect(Collectors.toSet());
    }

    private IndexRequest createUpdateRequest(Call record) {
        return new IndexRequest(indexName)
                .id(record.getId())
                .source(pojoToJSON(record), XContentType.JSON);
    }

    private String pojoToJSON(Call call) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = "";

        try {
            json = mapper.writeValueAsString(call);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
