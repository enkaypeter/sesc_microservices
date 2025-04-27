package dev.enkay.student_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.enkay.student_service.config.SecurityConfig;
import dev.enkay.student_service.dto.course.CourseResponseDto;
import dev.enkay.student_service.service.CourseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class) // Import your SecurityConfig
class CourseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourseService courseService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnListOfCoursesSuccessfully() throws Exception {
    // Arrange
    CourseResponseDto course1 = new CourseResponseDto(1L, "CSC701", "Advanced AI",
        "Deep learning, ML, real-world AI systems");
    CourseResponseDto course2 = new CourseResponseDto(2L, "CSC702", "Distributed Systems",
        "Architectures and performance");

    List<CourseResponseDto> courses = Arrays.asList(course1, course2);

    when(courseService.getAllCourses()).thenReturn(courses);

    // Act + Assert
    mockMvc.perform(get("/api/courses")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Courses retrieved successfully"))
        .andExpect(jsonPath("$.data.length()").value(2))
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].code").value("CSC701"))
        .andExpect(jsonPath("$.data[0].title").value("Advanced AI"))
        .andExpect(jsonPath("$.data[0].description").value("Deep learning, ML, real-world AI systems"))
        .andExpect(jsonPath("$.data[1].id").value(2))
        .andExpect(jsonPath("$.data[1].code").value("CSC702"))
        .andExpect(jsonPath("$.data[1].title").value("Distributed Systems"))
        .andExpect(jsonPath("$.data[1].description").value("Architectures and performance"))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}
