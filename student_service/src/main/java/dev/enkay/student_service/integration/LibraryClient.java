package dev.enkay.student_service.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class LibraryClient {
  private final WebClient webClient;

  public LibraryClient(WebClient libraryWebClient) {
    this.webClient = libraryWebClient;
  }

  public void registerStudent(String studentId) {
    RegisterMemberRequest req = new RegisterMemberRequest(studentId);
    webClient.post()
        .uri("/library/api/register")
        .bodyValue(req)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  @Data
  @AllArgsConstructor
  static class RegisterMemberRequest {
    private String studentId;
  }
}
