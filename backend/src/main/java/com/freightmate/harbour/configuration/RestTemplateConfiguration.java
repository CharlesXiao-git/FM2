package com.freightmate.harbour.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {

    private static final int DEFAULT_CONNECTION_TIMEOUT_SECONDS = 10;
    private static final int DEFAULT_READ_TIMEOUT_SECONDS = 55;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setReadTimeout(Duration.ofSeconds(DEFAULT_CONNECTION_TIMEOUT_SECONDS))
                .setConnectTimeout(Duration.ofSeconds(DEFAULT_READ_TIMEOUT_SECONDS))
                .build();
    }
}