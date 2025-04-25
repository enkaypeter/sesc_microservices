package dev.enkay.student_service.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data 
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank
  @Email
  public String email;

  @NotBlank
  public String password;
}
