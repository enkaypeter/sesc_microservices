package dev.enkay.student_service.dto.enrolment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentResponse {
    private Long id;
    private CourseInfoInEnrolment course;
}
