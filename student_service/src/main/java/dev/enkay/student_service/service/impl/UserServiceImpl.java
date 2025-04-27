package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.entity.User;
import dev.enkay.student_service.repository.UserRepository;
import dev.enkay.student_service.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No authenticated user.");
    }

    Object principal = authentication.getPrincipal();
    String email;

    if (principal instanceof User userPrincipal) {
      email = userPrincipal.getEmail();
    } else if (principal instanceof org.springframework.security.core.userdetails.User springUser) {
      email = springUser.getUsername();
    } else if (principal instanceof String str) {
      email = str;
    } else {
      throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }

    return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  @Override
  public User getUserByAuthentication(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No authenticated user found.");
    }

    String email = authentication.getName();
    return findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found."));
  }
}
