package dev.enkay.student_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.enkay.student_service.config.SecurityConfig;
import dev.enkay.student_service.dto.profile.ProfileResponse;
import dev.enkay.student_service.dto.profile.UpdateProfileRequest;
import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.security.JwtService;
import dev.enkay.student_service.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
@Import(SecurityConfig.class)
class ProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProfileService profileService;

  @MockBean
  private UserDetailsService userDetailsService; 

  @MockBean
  private JwtService jwtService;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldReturnProfileSuccessfully() throws Exception {
    ProfileResponse profile = new ProfileResponse(1L, "c1133445","user@example.com", "John", "Doe");

    when(profileService.getProfile()).thenReturn(profile);

    mockMvc.perform(get("/api/profile/me")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data.email").value("user@example.com"))
      .andExpect(jsonPath("$.data.firstName").value("John"))
      .andExpect(jsonPath("$.data.lastName").value("Doe"));
  }

  @Test
  @WithMockUser
  void shouldUpdateProfileSuccessfully() throws Exception {
    UpdateProfileRequest request = new UpdateProfileRequest("NewName", "NewSurname");
    ProfileResponse profile = new ProfileResponse(1L, "c1133440", "user@example.com", "NewName", "NewSurname");

    when(profileService.updateProfile(any())).thenReturn(profile);

    mockMvc.perform(put("/api/profile/me")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.success").value(true))
      .andExpect(jsonPath("$.data.firstName").value("NewName"))
      .andExpect(jsonPath("$.data.lastName").value("NewSurname"));
  }
}
