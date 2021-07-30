package com.example.elasticsearch;

import com.example.kafka.model.Call;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.elasticsearch.ESConfig.LOCAL_ES_CLIENT;
import static com.example.elasticsearch.ESConfig.AWS_ES_CLIENT;

@Component
public class ElasticSearchUpdater {

    private final String indexName;
    private final RestHighLevelClient client;

    public ElasticSearchUpdater(
            @Value("calls-pr") String indexName,
            @Qualifier(LOCAL_ES_CLIENT) RestHighLevelClient client
    ) {
        this.indexName = indexName;
        this.client = client;
    }

    /**
     * Sends a batch of records to ElasticSearch
     * Returns a set of failed ES record ids
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
