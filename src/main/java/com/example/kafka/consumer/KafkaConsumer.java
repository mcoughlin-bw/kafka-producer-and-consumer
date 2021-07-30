package com.example.kafka.consumer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.kafka.model.Call;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class KafkaConsumer {

    private final ElasticSearchUpdater updater;

    @KafkaListener(topics = "insights", groupId = "insights")
    public void consume(final Call call) {
        System.out.println("Consuming call with start time: " + call.getStartTime());

        List<Call> calls = new ArrayList<>();
        calls.add(call);

        updater.indexBatch(calls);
    }
}
