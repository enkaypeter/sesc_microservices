package dev.enkay.student_service.controller;

import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @GetMapping
  public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
    return ResponseEntity.ok(courseService.getAllCourses());
  }
}
