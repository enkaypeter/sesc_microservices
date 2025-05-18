package dev.enkay.student_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.enkay.student_service.config.SecurityConfig;
import dev.enkay.student_service.dto.enrolment.CourseInfoInEnrolment;
import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;
import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.security.JwtService;
import dev.enkay.student_service.service.EnrolmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrolmentController.class)
@Import(SecurityConfig.class)
class EnrolmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EnrolmentService enrolmentService;

  @MockBean
  private JwtService jwtService;

  @MockBean
  private UserRepository  userRepository;


  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldEnrolInCourseSuccessfully() throws Exception {
    EnrolmentRequest request = new EnrolmentRequest(1L);

    CourseInfoInEnrolment courseInfo = new CourseInfoInEnrolment(1L, "CSC701", "Advanced AI",
        "Deep learning, ML, real-world AI systems");
    EnrolmentResponse response = new EnrolmentResponse(1L, "c1122334", null, courseInfo);

    given(enrolmentService.enrolInCourse(any())).willReturn(response);

    mockMvc.perform(post("/api/enrolments")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Enrolled successfully."))
        .andExpect(jsonPath("$.data.course.code").value("CSC701"));
  }

  @Test
  @WithMockUser
  void shouldGetMyEnrolmentsSuccessfully() throws Exception {
    CourseInfoInEnrolment courseInfo = new CourseInfoInEnrolment(1L, "CSC701", "Advanced AI",
        "Deep learning, ML, real-world AI systems");
    EnrolmentResponse response = new EnrolmentResponse(1L, "c2233445", null, courseInfo);

    given(enrolmentService.getEnrolments()).willReturn(List.of(response));

    mockMvc.perform(get("/api/enrolments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Enrolments retrieved successfully."))
        .andExpect(jsonPath("$.data.length()").value(1))
        .andExpect(jsonPath("$.data[0].course.code").value("CSC701"));
  }
}
