package dev.enkay.student_service.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
  private Long id;
  private String studentId;
  private String email;
  private String firstName;
  private String lastName;
}
