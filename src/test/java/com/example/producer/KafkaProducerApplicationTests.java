package com.example.producer;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.kafka.model.Call;
import com.example.perf.EventProcessorPerfTest;
import com.example.perf.config.PerfConfiguration;

@EnableConfigurationProperties
@SpringBootTest(classes = {PerfConfiguration.class})
class KafkaProducerApplicationTests {

    @Autowired KafkaTemplate<String, Call> kafkaTemplate;

    @Autowired RestHighLevelClient elasticSearchClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void runPerfTest() throws Exception {
        final EventProcessorPerfTest eventProcessorPerfTest = new EventProcessorPerfTest(kafkaTemplate,
            elasticSearchClient);
        eventProcessorPerfTest.execute();
    }
}
