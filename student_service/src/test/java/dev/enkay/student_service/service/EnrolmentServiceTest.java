package dev.enkay.student_service.service;

import dev.enkay.student_service.dto.enrolment.CourseInfoInEnrolment;
import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;
import dev.enkay.student_service.entity.Course;
import dev.enkay.student_service.entity.Enrolment;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.entity.User;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.repository.EnrolmentRepository;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.impl.EnrolmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class EnrolmentServiceTest {

  @InjectMocks
  private EnrolmentServiceImpl enrolmentService;

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private EnrolmentRepository enrolmentRepository;

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private UserService userService;

  private User mockUser;
  private Course mockCourse;
  private Student mockStudent;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail("student@example.com");
    mockUser.setRole("STUDENT");

    mockStudent = new Student();
    mockStudent.setId(1L);
    mockStudent.setUser(mockUser);

    mockCourse = new Course();
    mockCourse.setId(1L);
    mockCourse.setCode("CSC701");
    mockCourse.setTitle("Advanced AI");
    mockCourse.setDescription("Deep learning, ML, real-world AI systems");
  }

  @Test
  void shouldEnrolInCourseSuccessfully() {
    // Arrange
    EnrolmentRequest request = new EnrolmentRequest(mockCourse.getId());

    given(userService.getCurrentUser()).willReturn(mockUser);
    given(studentRepository.findByUser(mockUser)).willReturn(Optional.of(mockStudent));
    given(courseRepository.findById(mockCourse.getId())).willReturn(Optional.of(mockCourse));
    given(enrolmentRepository.findByStudentAndCourse(mockStudent, mockCourse)).willReturn(Optional.empty());
    given(enrolmentRepository.save(any(Enrolment.class)))
        .willAnswer(invocation -> invocation.getArgument(0));

    // Act
    EnrolmentResponse response = enrolmentService.enrolInCourse(request);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.getCourse()).isNotNull();
    assertThat(response.getCourse().getCode()).isEqualTo(mockCourse.getCode());
    assertThat(response.getCourse().getTitle()).isEqualTo(mockCourse.getTitle());
    assertThat(response.getCourse().getDescription()).isEqualTo(mockCourse.getDescription());
  }

  @Test
  void shouldGetMyEnrolmentsSuccessfully() {
    // Arrange
    Enrolment enrolment = new Enrolment();
    enrolment.setId(1L);
    enrolment.setStudent(mockStudent);
    enrolment.setCourse(mockCourse);

    given(userService.getCurrentUser()).willReturn(mockUser);
    given(studentRepository.findByUser(mockUser)).willReturn(Optional.of(mockStudent));
    given(enrolmentRepository.findByStudent(mockStudent)).willReturn(List.of(enrolment));

    // Act
    List<EnrolmentResponse> responses = enrolmentService.getEnrolments();

    // Assert
    assertThat(responses).hasSize(1);
    assertThat(responses.get(0).getCourse().getCode()).isEqualTo(mockCourse.getCode());
  }
}
