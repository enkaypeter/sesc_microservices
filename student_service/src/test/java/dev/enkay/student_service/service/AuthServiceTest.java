package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.auth.AuthResponse;
import dev.enkay.student_service.dto.auth.LoginRequest;
import dev.enkay.student_service.dto.auth.RegisterRequest;
import dev.enkay.student_service.entity.User;

import static dev.enkay.student_service.utils.TestAuthConstants.*;

import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.security.JwtService;
import dev.enkay.student_service.service.impl.AuthServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;


    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(MOCK_ENCODED_PASSWORD);
        user.setRole("STUDENT");

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn(MOCK_ENCODED_PASSWORD);
        given(userRepository.save(any(User.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(jwtService.generateToken(any(User.class))).willReturn(MOCK_JWT_TOKEN);
        given(jwtService.getExpiry(anyString())).willReturn(86400L);


        AuthResponse response = authService.register(request);

        assertThat(response.getToken().getAccessToken()).isEqualTo(MOCK_JWT_TOKEN);
        assertThat(response.getToken().getExpiresAt()).isEqualTo(86400L);
        assertThat(response.getUser().getEmail()).isEqualTo(MOCK_EMAIL);
        assertThat(response.getUser().getRole()).isEqualTo("STUDENT");
        assertThat(response.getMessage()).isEqualTo("Registration successful");
    }

    @Test
    void shouldLoginUserSuccessfully() {
        LoginRequest request = new LoginRequest(MOCK_EMAIL, MOCK_RAW_PASSWORD);
    
        User user = new User();
        user.setEmail(MOCK_EMAIL);
        user.setPassword(MOCK_ENCODED_PASSWORD);
        user.setRole("STUDENT");
    
        // Simulate successful authentication
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(user, null));
    
        when(userRepository.findByEmail(MOCK_EMAIL)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(MOCK_JWT_TOKEN);
    
        AuthResponse response = authService.login(request);
    
        assertEquals(MOCK_JWT_TOKEN, response.getToken().getAccessToken());
        assertEquals(MOCK_LOGIN_SUCCESS, response.getMessage());
    }
    }
