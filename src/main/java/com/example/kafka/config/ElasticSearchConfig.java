package com.example.kafka.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

//    @Value("${aws.es.endpoint}")
    private String elasticSearchEndpoint = "https://vpc-call-search-poc-zejyc6ozsrv4y2dl35fl5hnesm.us-east-1.es.amazonaws.com";

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticSearchEndpoint))
        );
    }
}
