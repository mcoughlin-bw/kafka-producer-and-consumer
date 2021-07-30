package com.example.kafka.consumer;

import com.example.elasticsearch.ESConfig;
import com.example.elasticsearch.ElasticSearchUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.kafka.model.Call;
import static com.example.elasticsearch.ESConfig.LOCAL_ES_CLIENT;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "insights", groupId = "insights")
    public void consume(final Call call) {
        System.out.println("Consuming call with start time: " + call.getStartTime());

        ElasticSearchUpdater updater = new ElasticSearchUpdater("insights", ESConfig.localRestHighLevelClient());
        List<Call> calls = new ArrayList<>();
        calls.add(call);

        updater.indexBatch(calls);
    }
}
