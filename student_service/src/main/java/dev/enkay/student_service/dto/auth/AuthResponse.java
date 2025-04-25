package dev.enkay.student_service.dto.auth;

import lombok.Data;

@Data
public class AuthResponse {
  private TokenInfo token;
  private UserInfo user;
  private String message;

  public AuthResponse(TokenInfo token, UserInfo user, String message) {
    this.token = token;
    this.user = user;
    this.message = message;
  }

  public TokenInfo getToken() {
    return token;
  }

  public UserInfo getUser() {
    return user;
  }

  public String getMessage() {
    return message;
  }
}
