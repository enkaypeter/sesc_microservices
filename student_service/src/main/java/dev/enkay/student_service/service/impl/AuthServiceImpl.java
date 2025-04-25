package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.auth.RegisterRequest;
import dev.enkay.student_service.dto.auth.TokenInfo;
import dev.enkay.student_service.dto.auth.UserInfo;
import dev.enkay.student_service.dto.auth.LoginRequest;
import dev.enkay.student_service.dto.auth.AuthResponse;

import dev.enkay.student_service.entity.User;
import dev.enkay.student_service.repository.UserRepository;

import dev.enkay.student_service.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.enkay.student_service.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthServiceImpl(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @Override
  public AuthResponse register(RegisterRequest request) {
    // TODO: return a custom response object
    if (userRepository.existsByEmail(request.email)) {
      throw new IllegalArgumentException("Email is already registered.");
    }

    User user = new User();
    user.setEmail(request.email);
    user.setPassword(passwordEncoder.encode(request.password));
    user.setRole("STUDENT"); // default role

    userRepository.save(user);

    String token = jwtService.generateToken(user);
    Long expiry = jwtService.getExpiry(token);

    TokenInfo tokenInfo = new TokenInfo(token, expiry);
    UserInfo userInfo = new UserInfo(user.getId(), user.getEmail(), user.getRole());

    return new AuthResponse(tokenInfo, userInfo, "Registration successful");
  }

  @Override
  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email, request.password));
    User user = userRepository.findByEmail(request.email)
      .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
  
    String token = jwtService.generateToken(user);
    Long expiry = jwtService.getExpiry(token);

    return new AuthResponse(new TokenInfo(token, expiry), 
      new UserInfo(user.getId(), user.getEmail(), user.getRole()), 
      "Login successful");
  }
}
