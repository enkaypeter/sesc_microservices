package dev.enkay.student_service.controller;


import dev.enkay.student_service.config.SecurityConfig;
import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.security.JwtService;
import dev.enkay.student_service.service.CourseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class)
class CourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourseService courseService;

  @MockBean
  private CourseRepository  courseRepository;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private UserRepository  userRepository;

  @Test
  void shouldReturnListOfCoursesSuccessfully() throws Exception {
    // Arrange
    CourseResponseDto course1 = new CourseResponseDto(1L, "CSC701", "Advanced AI", "Deep learning, ML, real-world AI systems");
    CourseResponseDto course2 = new CourseResponseDto(2L, "CSC702", "Distributed Systems", "Architectures and performance");

    List<CourseResponseDto> courses = Arrays.asList(course1, course2);

    when(courseService.getAllCourses()).thenReturn(courses);

    // Act
    ResultActions resultActions = mockMvc.perform(get("/api/courses")
      .contentType(MediaType.APPLICATION_JSON));
    
    // Assert
    resultActions.andExpect(status().isOk())
    .andExpectAll(
        jsonPath("$.success").value(true),
        jsonPath("$.message").value("Courses retrieved successfully"),
        jsonPath("$.data.length()").value(2),
        jsonPath("$.data[0].id").value(1),
        jsonPath("$.data[0].code").value("CSC701"),
        jsonPath("$.data[0].title").value("Advanced AI"),
        jsonPath("$.data[0].description").value("Deep learning, ML, real-world AI systems"),
        jsonPath("$.data[1].id").value(2),
        jsonPath("$.data[1].code").value("CSC702"),
        jsonPath("$.data[1].title").value("Distributed Systems"),
        jsonPath("$.data[1].description").value("Architectures and performance"),
        jsonPath("$.timestamp").exists());
  }
}
