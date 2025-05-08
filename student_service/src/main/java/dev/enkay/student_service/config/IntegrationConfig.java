package dev.enkay.student_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class IntegrationConfig {

  @Bean
  public WebClient gatewayClient(@Value("${services.gateway.base-url}") String baseUrl) {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .build();
  }
}
