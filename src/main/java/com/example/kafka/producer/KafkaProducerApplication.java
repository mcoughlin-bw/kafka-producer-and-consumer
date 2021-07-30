package com.example.kafka.producer;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.example.kafka.model.Call;

@SpringBootApplication
@EnableScheduling
public class KafkaProducerApplication {

    @Autowired KafkaTemplate<String, Call> kafkaTemplate;

    @Scheduled(fixedRate = 10_000)
    public void sendMessage() {
        final Call call = new Call();
        call.setStartTime(new Date());

        final ListenableFuture<SendResult<String, Call>> future =
            kafkaTemplate.send("insights", call);

        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(final SendResult<String, Call> result) {
                System.out.println("Sent call with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(final Throwable ex) {
                System.out.println("Unable to send call due to : " + ex.getMessage());
            }
        });
    }

    public static void main(final String[] args) {
        SpringApplication.run(KafkaProducerApplication.class, args);
    }
}
