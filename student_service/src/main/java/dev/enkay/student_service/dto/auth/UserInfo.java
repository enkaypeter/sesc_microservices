package dev.enkay.student_service.dto.auth;

import lombok.Data;

@Data 
public class UserInfo {
  private Long id;
  private String email;
  private String role;

  public UserInfo(Long id, String email, String role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }
}
