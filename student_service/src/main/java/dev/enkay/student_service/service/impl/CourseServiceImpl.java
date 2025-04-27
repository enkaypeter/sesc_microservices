package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.entity.Course;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

  private final CourseRepository courseRepository;

  @Autowired
  public CourseServiceImpl(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Override
  public List<CourseResponseDto> getAllCourses() {
    return courseRepository.findAll()
        .stream()
        .map(course -> new CourseResponseDto(
            course.getId(),
            course.getCode(),
            course.getTitle(),
            course.getDescription()))
        .collect(Collectors.toList());
  }
}
