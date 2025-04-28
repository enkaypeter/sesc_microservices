package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;

public interface GraduationService {
  GraduationEligibilityResponse checkGraduationEligibility();
}
