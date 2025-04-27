package dev.enkay.student_service.controller;

import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;
import dev.enkay.student_service.common.ApiResponse;
import dev.enkay.student_service.service.EnrolmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/enrolments")
@Tag(name = "Enrolments", description = "Endpoints for course enrolment management")
public class EnrolmentController {

  private final EnrolmentService enrolmentService;

  public EnrolmentController(EnrolmentService enrolmentService) {
    this.enrolmentService = enrolmentService;
  }

  @Operation(summary = "Enrol in a course")
  @PostMapping
  public ResponseEntity<ApiResponse<EnrolmentResponse>> enrolInCourse(@RequestBody EnrolmentRequest request) {
    var response = enrolmentService.enrolInCourse(request);
    return ResponseEntity.status(201).body(
        ApiResponse.<EnrolmentResponse>builder()
            .success(true)
            .message("Enrolled successfully.")
            .data(response)
            .timestamp(Instant.now())
            .build());
  }

  @Operation(summary = "View my enrolments")
  @GetMapping()
  public ResponseEntity<ApiResponse<List<EnrolmentResponse>>> getMyEnrolments() {
    var response = enrolmentService.getEnrolments();
    return ResponseEntity.ok(
        ApiResponse.<List<EnrolmentResponse>>builder()
            .success(true)
            .message("Enrolments retrieved successfully.")
            .data(response)
            .timestamp(Instant.now())
            .build());
  }
}
