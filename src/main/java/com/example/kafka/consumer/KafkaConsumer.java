package com.example.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.kafka.model.Call;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "insights", groupId = "insights")
    public void consume(final Call call) {
        System.out.println("Consuming call with start time: " + call.getStartTime());
    }
}
