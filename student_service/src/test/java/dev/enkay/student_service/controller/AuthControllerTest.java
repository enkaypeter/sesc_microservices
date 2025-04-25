package dev.enkay.student_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enkay.student_service.dto.auth.AuthResponse;
import dev.enkay.student_service.dto.auth.LoginRequest;
import dev.enkay.student_service.dto.auth.RegisterRequest;
import dev.enkay.student_service.dto.auth.TokenInfo;
import dev.enkay.student_service.dto.auth.UserInfo;
import dev.enkay.student_service.service.AuthService;
import dev.enkay.student_service.config.SecurityConfig;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static dev.enkay.student_service.utils.TestAuthConstants.*;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)

class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser
  void shouldRegisterSuccessfully() throws Exception {
      RegisterRequest request = new RegisterRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);

      TokenInfo tokenInfo = new TokenInfo(MOCK_JWT_TOKEN, 86400L);
      UserInfo userInfo = new UserInfo(MOCK_USER_ID, MOCK_EMAIL, "STUDENT");
      AuthResponse response = new AuthResponse(tokenInfo, userInfo, "Registration successful");

      when(authService.register(any())).thenReturn(response);

      mockMvc.perform(post("/api/auth/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(request)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token.accessToken").value(MOCK_JWT_TOKEN))
          .andExpect(jsonPath("$.token.expiresAt").value(86400))
          .andExpect(jsonPath("$.user.email").value(MOCK_EMAIL))
          .andExpect(jsonPath("$.user.role").value("STUDENT"))
          .andExpect(jsonPath("$.message").value("Registration successful"));
  }

  @Test
  @WithMockUser
  void shouldLoginSuccessfully() throws Exception {
    // Arrange
    LoginRequest request = new LoginRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);

    TokenInfo tokenInfo = new TokenInfo(MOCK_JWT_TOKEN, 86400L);
    UserInfo userInfo = new UserInfo(MOCK_USER_ID, MOCK_EMAIL, "STUDENT");

    AuthResponse response = new AuthResponse(tokenInfo, userInfo, MOCK_LOGIN_SUCCESS);

    when(authService.login(any())).thenReturn(response);

    // Act + Assert
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token.accessToken").value(MOCK_JWT_TOKEN))
        .andExpect(jsonPath("$.token.expiresAt").value(86400))
        // .andExpect(jsonPath("$.user.id").value(MOCK_USER_ID))
        .andExpect(jsonPath("$.user.email").value(MOCK_EMAIL))
        .andExpect(jsonPath("$.user.role").value("STUDENT"))
        .andExpect(jsonPath("$.message").value(MOCK_LOGIN_SUCCESS));
  }

}
