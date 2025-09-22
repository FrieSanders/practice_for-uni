package com.eltech.orderservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpConfig {
    @Bean RestClient restClient(RestClient.Builder b){ return b.build(); }
}
