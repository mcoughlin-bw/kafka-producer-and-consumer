package com.example.kafka.consumer;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.kafka.model.Call;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, Call> consumerFactory() {
        final Map<String, Object> props = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG, "insights",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            JsonDeserializer.TRUSTED_PACKAGES, "com.example.kafka.model");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Call> kafkaListenerContainerFactory(final ConsumerFactory consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<String, Call> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}