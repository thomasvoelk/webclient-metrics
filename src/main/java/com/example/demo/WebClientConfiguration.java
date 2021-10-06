package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient(WebClient.Builder builder, LogResponseTimeFilterFunction logResponseTimeFilterFunction) {
        return builder
                .filter(logResponseTimeFilterFunction)
                .build();
    }
}
