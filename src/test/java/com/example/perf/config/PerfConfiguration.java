package com.example.perf.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.example.kafka.consumer", "com.example.kafka.producer"})
public class PerfConfiguration {
}
