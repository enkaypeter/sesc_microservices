package dev.enkay.student_service.dto.auth;

public class TokenInfo {
  private String accessToken;
  private Long expiresAt;

  public TokenInfo(String accessToken, Long expiresAt) {
    this.accessToken = accessToken;
    this.expiresAt = expiresAt;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }
}
