package dev.enkay.student_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.enkay.student_service.dto.auth.LoginRequest;
import dev.enkay.student_service.dto.auth.RegisterRequest;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static dev.enkay.student_service.utils.TestAuthConstants.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

  @Container
  static PostgreSQLContainer<?> postgres =
    new PostgreSQLContainer<>("postgres:14")
      .withDatabaseName("students")
      .withUsername("sa")
      .withPassword("sa");

  // Wire the container’s JDBC URL into Spring’s datasource
  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void registerShouldPersistAndReturnTokenAndUser() throws Exception {
    var request = new RegisterRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token.accessToken").isNotEmpty())
      .andExpect(jsonPath("$.token.expiresAt").isNumber())
      .andExpect(jsonPath("$.user.email").value(MOCK_EMAIL))
      .andExpect(jsonPath("$.user.id").isNumber())
      .andExpect(jsonPath("$.user.role").value("STUDENT"))
      .andExpect(jsonPath("$.message").value("Registration successful"));    
  }

  @Test
  void loginShouldReturnExistingUserToken() throws Exception {
    // First register so there is an account to log into
    var register = new RegisterRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);
    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(register)))
        .andExpect(status().isOk());

    var loginReq = new LoginRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginReq)))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.token.accessToken").isNotEmpty())
      .andExpect(jsonPath("$.user.email").value(MOCK_EMAIL))
      .andExpect(jsonPath("$.user.role").value("STUDENT"))
      .andExpect(jsonPath("$.message").value(MOCK_LOGIN_SUCCESS));
  }
}
