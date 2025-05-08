package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.finance.InvoiceResponse;
import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;
import dev.enkay.student_service.integration.FinanceClient;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.GraduationService;
import dev.enkay.student_service.service.UserService;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GraduationServiceImpl implements GraduationService {

  private final UserService userService;
  private final StudentRepository studentRepository;
  private final FinanceClient financeWebClient;

  public GraduationServiceImpl(UserService userService, StudentRepository studentRepository, FinanceClient financeClient) {
    this.userService = userService;
    this.studentRepository = studentRepository;
    this.financeWebClient = financeClient;
  }

  @Override
  public GraduationEligibilityResponse checkGraduationEligibility() {
    var user = userService.getCurrentUser();
    var student = studentRepository.findByUser(user)
      .orElseThrow(() -> new IllegalArgumentException("Student profile not found."));
    
    String studentId = student.getStudentId();

    List<InvoiceResponse> invoices = financeWebClient.fetchAllInvoices();

    // 3. see if any are OUTSTANDING for this student
    boolean hasOutstanding = invoices.stream()
        .anyMatch(inv ->
            studentId.equals(inv.getStudentId())
            && "OUTSTANDING".equalsIgnoreCase(inv.getStatus())
        );

    // 4. eligible only if none outstanding
    return new GraduationEligibilityResponse(studentId, !hasOutstanding);
  }
}
