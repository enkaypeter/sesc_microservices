package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.GraduationService;
import dev.enkay.student_service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class GraduationServiceImpl implements GraduationService {

  private final UserService userService;
  private final StudentRepository studentRepository;

  public GraduationServiceImpl(UserService userService, StudentRepository studentRepository) {
    this.userService = userService;
    this.studentRepository = studentRepository;
  }

  @Override
  public GraduationEligibilityResponse checkGraduationEligibility() {
    var user = userService.getCurrentUser();

    studentRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("Student profile not found."));

    // TODO: Replace this mock logic later when we integrate with the Finance Service.
    boolean hasOutstandingInvoices = false; // simulate no unpaid invoices for now

    boolean eligible = !hasOutstandingInvoices;

    return GraduationEligibilityResponse.builder()
      .eligible(eligible)
      .build();
  }
}
