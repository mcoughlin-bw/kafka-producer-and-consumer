package com.example.kafka.producer;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.kafka.model.Call;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Call> producerFactory() {
        final Map<String, Object> configProps = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Call> kafkaTemplate(final ProducerFactory producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
