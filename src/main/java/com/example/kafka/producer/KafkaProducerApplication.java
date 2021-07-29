package com.example.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@SpringBootApplication
@EnableScheduling
public class KafkaProducerApplication {

    @Autowired KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 10_000)
    public void sendMessage() {
        final String message = "Message: " + System.currentTimeMillis();

        final ListenableFuture<SendResult<String, String>> future =
            kafkaTemplate.send("insights", message);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(final SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                    "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(final Throwable ex) {
                System.out.println("Unable to send message=["
                    + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public static void main(final String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }
}
