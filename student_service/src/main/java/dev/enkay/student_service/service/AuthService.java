package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.auth.RegisterRequest;
import dev.enkay.student_service.dto.auth.LoginRequest;
import dev.enkay.student_service.dto.auth.AuthResponse;

public interface AuthService {
  String register(RegisterRequest request);
  AuthResponse login(LoginRequest request);
}