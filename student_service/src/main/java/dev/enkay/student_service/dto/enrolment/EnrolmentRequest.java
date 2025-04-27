package dev.enkay.student_service.dto.enrolment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentRequest {
    private Long courseId;
}
