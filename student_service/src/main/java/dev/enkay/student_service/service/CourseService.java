package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.course.CourseResponseDto;

import java.util.List;

public interface CourseService {
  List<CourseResponseDto> getAllCourses();
}
