package dev.enkay.student_service.dto.enrolment;

import dev.enkay.student_service.dto.finance.InvoiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentResponse {
    private Long id;
    private String studentId;
    private InvoiceResponse invoice;
    private CourseInfoInEnrolment course;
}
