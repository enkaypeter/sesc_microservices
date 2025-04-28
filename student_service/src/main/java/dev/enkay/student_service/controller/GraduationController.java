package dev.enkay.student_service.controller;

import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;
import dev.enkay.student_service.common.ApiResponse;
import dev.enkay.student_service.service.GraduationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/graduation")
@Tag(name = "Graduation", description = "Endpoints related to graduation eligibility")
public class GraduationController {

  private final GraduationService graduationService;

  public GraduationController(GraduationService graduationService) {
    this.graduationService = graduationService;
  }

  @Operation(
    summary = "Check Graduation Eligibility",
    description = "Returns whether the currently authenticated student is eligible to graduate."
  )
  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Eligibility status retrieved successfully")
  @GetMapping("/eligibility")
  public ResponseEntity<ApiResponse<GraduationEligibilityResponse>> checkGraduationEligibility() {
    GraduationEligibilityResponse eligibility = graduationService.checkGraduationEligibility();

    var response = ApiResponse.<GraduationEligibilityResponse>builder()
      .success(true)
      .message("Graduation eligibility retrieved successfully")
      .data(eligibility)
      .timestamp(Instant.now())
      .build();

    return ResponseEntity.ok(response);
  }
}
