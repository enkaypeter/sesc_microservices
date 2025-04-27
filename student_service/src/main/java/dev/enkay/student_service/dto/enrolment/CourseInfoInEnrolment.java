package dev.enkay.student_service.dto.enrolment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfoInEnrolment {
    private Long id;
    private String code;
    private String title;
    private String description;
}
