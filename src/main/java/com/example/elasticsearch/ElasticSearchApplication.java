package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ElasticSearchApplication {

    public static void main(final String[] args) {
        SpringApplication.run(com.example.elasticsearch.ElasticSearchUpdater.class, args);
    }
}
