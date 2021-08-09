package com.example.kafka.producer;

//TODO: doesn't run in AWS but useful for local testing
// @Configuration
public class KafkaTopicConfig {

    //    @Value("${kafka.bootstrapAddress}")
    //    private String bootstrapAddress;
    //
    //    @Bean
    //    public KafkaAdmin kafkaAdmin() {
    //        final Map<String, Object> config = Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    //        return new KafkaAdmin(config);
    //    }
    //
    //    @Bean
    //    public NewTopic newTopic() {
    //        return new NewTopic("insights", 1, (short) 1);
    //    }
}
