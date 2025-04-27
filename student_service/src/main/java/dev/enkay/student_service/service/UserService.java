package dev.enkay.student_service.service;

import dev.enkay.student_service.entity.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    User getCurrentUser();
    User getUserByAuthentication(Authentication authentication);
}
