package dev.enkay.student_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
  private final Jwt jwt = new Jwt();

  @Data
  public static class Jwt {
    private String secret;
    private Long expiration;
  }
}
