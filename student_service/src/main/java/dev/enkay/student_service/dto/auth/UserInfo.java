package dev.enkay.student_service.dto.auth;

public class UserInfo {
  private String email;
  private String role;
  // private String firstName;
  // private String lastName;

  public UserInfo(String email, String role) {
    this.email = email;
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public String getRole() {
    return role;
  }

  // public String getFirstName() {
  //   return firstName;
  // }

  // public String getLastName() {
  //   return lastName;
  // }
}
