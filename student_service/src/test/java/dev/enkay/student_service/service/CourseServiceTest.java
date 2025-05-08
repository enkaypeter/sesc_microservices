package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.entity.Course;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CourseServiceTest {

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private CourseServiceImpl courseService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnAllCoursesSuccessfully() {
    // Arrange
    Course course1 = new Course(1L, "CSC701", "Advanced AI", "Deep learning, ML, real-world AI systems", 300.0);
    Course course2 = new Course(2L, "CSC702", "Distributed Systems", "Architectures and performance", 350.0);

    List<Course> courses = Arrays.asList(course1, course2);

    when(courseRepository.findAll()).thenReturn(courses);

    // Act
    List<CourseResponseDto> result = courseService.getAllCourses();

    // Assert
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getId()).isEqualTo(1L);
    assertThat(result.get(0).getCode()).isEqualTo("CSC701");
    assertThat(result.get(0).getTitle()).isEqualTo("Advanced AI");
    assertThat(result.get(0).getDescription()).isEqualTo("Deep learning, ML, real-world AI systems");

    assertThat(result.get(1).getId()).isEqualTo(2L);
    assertThat(result.get(1).getCode()).isEqualTo("CSC702");
    assertThat(result.get(1).getTitle()).isEqualTo("Distributed Systems");
    assertThat(result.get(1).getDescription()).isEqualTo("Architectures and performance");
  }
}
