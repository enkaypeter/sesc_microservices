package dev.enkay.student_service.controller;

import dev.enkay.student_service.common.ApiResponse;
import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getAllCourses() {
    ApiResponse<List<CourseResponseDto>> response = ApiResponse.<List<CourseResponseDto>>builder()
    .success(true)
    .message("Courses retrieved successfully")
    .data(courseService.getAllCourses())
    .timestamp(Instant.now())
    .build();

    return ResponseEntity.ok(response);
  }
}
