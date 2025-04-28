package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.profile.ProfileResponse;
import dev.enkay.student_service.dto.profile.UpdateProfileRequest;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.entity.User;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private ProfileServiceImpl profileService;

  private static final String MOCK_EMAIL = "user@example.com";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnProfileSuccessfully() {
    User user = new User();
    user.setId(1L);
    user.setEmail(MOCK_EMAIL);

    Student student = new Student();
    student.setId(1L);
    student.setUser(user);
    student.setFirstName("John");
    student.setLastName("Doe");

    when(userService.getCurrentUser()).thenReturn(user);
    when(studentRepository.findByUser(user)).thenReturn(Optional.of(student));

    ProfileResponse response = profileService.getProfile();

    assertThat(response.getStudentId()).isEqualTo(1L);
    assertThat(response.getEmail()).isEqualTo(MOCK_EMAIL);
    assertThat(response.getFirstName()).isEqualTo("John");
    assertThat(response.getLastName()).isEqualTo("Doe");
  }

  @Test
  void shouldUpdateProfileSuccessfully() {
    User user = new User();
    user.setId(1L);
    user.setEmail(MOCK_EMAIL);

    Student student = new Student();
    student.setId(1L);
    student.setUser(user);
    student.setFirstName("OldName");
    student.setLastName("OldSurname");

    UpdateProfileRequest updateRequest = new UpdateProfileRequest("NewName", "NewSurname");

    when(userService.getCurrentUser()).thenReturn(user);
    when(studentRepository.findByUser(user)).thenReturn(Optional.of(student));
    when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArguments()[0]);

    ProfileResponse updated = profileService.updateProfile(updateRequest);

    assertThat(updated.getFirstName()).isEqualTo("NewName");
    assertThat(updated.getLastName()).isEqualTo("NewSurname");
  }
}
