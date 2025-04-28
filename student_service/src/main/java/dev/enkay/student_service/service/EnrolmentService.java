package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;

import java.util.List;

public interface EnrolmentService {
  EnrolmentResponse enrolInCourse(EnrolmentRequest request);
  List<EnrolmentResponse> getEnrolments();
}
