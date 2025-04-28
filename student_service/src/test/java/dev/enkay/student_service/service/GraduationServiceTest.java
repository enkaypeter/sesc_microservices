package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.graduation.GraduationEligibilityResponse;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.entity.User;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.impl.GraduationServiceImpl;
import dev.enkay.student_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GraduationServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private StudentRepository studentRepository;

  @InjectMocks
  private GraduationServiceImpl graduationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnEligibleWhenNoOutstandingInvoices() {
    // Arrange
    User user = new User();
    user.setId(1L);
    user.setEmail("student@example.com");

    Student student = new Student();
    student.setId(1L);
    student.setUser(user);

    when(userService.getCurrentUser()).thenReturn(user);
    when(studentRepository.findByUser(user)).thenReturn(Optional.of(student));

    // Act
    GraduationEligibilityResponse response = graduationService.checkGraduationEligibility();

    // Assert
    assertThat(response.isEligible()).isTrue();
  }
}
