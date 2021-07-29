package com.example.kafka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaConsumerApplication {
    
    public static void main(final String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class, args);
    }
}
