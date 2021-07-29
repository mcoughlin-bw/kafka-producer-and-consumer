package com.example.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = "insights", groupId = "insights")
    public void consume(final String message) {
        System.out.println("Consuming message: " + message);
    }
}
