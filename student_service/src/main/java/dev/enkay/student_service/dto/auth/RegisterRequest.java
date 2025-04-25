package dev.enkay.student_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class RegisterRequest {
  @NotBlank
  @Email
  public String email;

  @NotBlank
  @Size(min = 6, message = "Password must be at least 6 characters long")
  public String password;
}
