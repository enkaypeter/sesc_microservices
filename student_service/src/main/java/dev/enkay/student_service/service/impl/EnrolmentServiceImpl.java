package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.CourseInfoInEnrolment;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;
import dev.enkay.student_service.entity.Enrolment;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.events.StudentEventPublisher;
import dev.enkay.student_service.integration.FinanceClient;
import dev.enkay.student_service.integration.LibraryClient;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.repository.EnrolmentRepository;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.EnrolmentService;
import dev.enkay.student_service.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EnrolmentServiceImpl implements EnrolmentService {

  private final CourseRepository courseRepository;
  private final EnrolmentRepository enrolmentRepository;
  private final StudentRepository studentRepository;
  private final UserService userService;
  private final StudentEventPublisher studentEventPublisher;

  private final FinanceClient financeClient;
  private final LibraryClient libraryClient;

  public EnrolmentServiceImpl(CourseRepository courseRepository, EnrolmentRepository enrolmentRepository,
      StudentRepository studentRepository, UserService userService, StudentEventPublisher studentEventPublisher,
      FinanceClient financeClient, LibraryClient libraryClient) {
    this.courseRepository = courseRepository;
    this.enrolmentRepository = enrolmentRepository;
    this.studentRepository = studentRepository;
    this.userService = userService;
    this.financeClient = financeClient;
    this.libraryClient = libraryClient;
    this.studentEventPublisher = studentEventPublisher;
  }

  public static String generateStudentId(StudentRepository studentRepository) {
    int randomNumber = new Random().nextInt(9000000) + 1000000;
    String studentId = "c" + randomNumber;

    if (studentRepository.existsByStudentId(studentId)) {
      return generateStudentId(studentRepository);
    }

    return studentId;
  }

  @Override
  @Transactional
  public EnrolmentResponse enrolInCourse(EnrolmentRequest request) {
    var user = userService.getCurrentUser();

    // Get or create student account
    var student = studentRepository.findByUser(user)
        .orElseGet(() -> {
          Student newStudent = new Student();
          newStudent.setUser(user);
          Student savedStudent = studentRepository.save(newStudent);

          String studentId = EnrolmentServiceImpl.generateStudentId(studentRepository);
          savedStudent.setStudentId(studentId);

          // TODO: wrap in events to make it async
          financeClient.registerStudent(studentId);
          libraryClient.registerStudent(studentId);

          // TODO: publish event to other services using AMQP
          // studentEventPublisher
          // .publishStudentCreated(new StudentCreatedEvent(savedStudent.getId(), "",
          // user.getEmail()));
          return savedStudent;
        });

    var course = courseRepository.findById(request.getCourseId())
        .orElseThrow(() -> new IllegalArgumentException("Course not found."));

    enrolmentRepository.findByStudentAndCourse(student, course)
        .ifPresent(e -> {
          throw new IllegalStateException("Already enrolled in this course.");
        });

    var enrolment = new Enrolment();
    enrolment.setStudent(student);
    enrolment.setCourse(course);

    var savedEnrolment = enrolmentRepository.save(enrolment);

    double tuitionAmount = course.getFees(); // assuming Course has `fee` field
    String dueDate = LocalDate.now().plusMonths(1).toString();
    var invoice = financeClient.createInvoice(student.getStudentId(), tuitionAmount, dueDate);

    return new EnrolmentResponse(
        savedEnrolment.getId(),
        student.getStudentId(),
        invoice,
        new CourseInfoInEnrolment(
            course.getId(),
            course.getCode(),
            course.getTitle(),
            course.getDescription()));
  }

  @Override
  public List<EnrolmentResponse> getEnrolments() {
    var user = userService.getCurrentUser();
    var student = studentRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("Student profile not found."));

    var enrolments = enrolmentRepository.findByStudent(student);

    return enrolments.stream()
        .map(e -> new EnrolmentResponse(
            e.getId(),
            student.getStudentId(),
            null,
            new CourseInfoInEnrolment(
                e.getCourse().getId(),
                e.getCourse().getCode(),
                e.getCourse().getTitle(),
                e.getCourse().getDescription())))
        .collect(Collectors.toList());
  }
}
