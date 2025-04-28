package dev.enkay.student_service.controller;

import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;
import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.security.JwtService;
import dev.enkay.student_service.service.GraduationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GraduationController.class)
class GraduationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GraduationService graduationService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtService jwtService;


  @Test
  @WithMockUser
  void shouldReturnGraduationEligibilitySuccessfully() throws Exception {
    // Arrange
    GraduationEligibilityResponse eligibilityResponse = GraduationEligibilityResponse.builder()
      .eligible(true)
      .build();

    when(graduationService.checkGraduationEligibility()).thenReturn(eligibilityResponse);

    // Act + Assert
    mockMvc.perform(get("/api/graduation/eligibility")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.message").value("Graduation eligibility retrieved successfully"))
      .andExpect(jsonPath("$.data.eligible").value(true))
      .andExpect(jsonPath("$.timestamp").exists());
  }
}
