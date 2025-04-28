package dev.enkay.student_service.dto.graduation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduationEligibilityResponse {
  private boolean eligible;
}
