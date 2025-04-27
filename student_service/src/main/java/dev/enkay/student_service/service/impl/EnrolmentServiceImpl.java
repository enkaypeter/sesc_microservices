package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.enrolment.EnrolmentRequest;
import dev.enkay.student_service.dto.enrolment.CourseInfoInEnrolment;
import dev.enkay.student_service.dto.enrolment.EnrolmentResponse;
import dev.enkay.student_service.entity.Enrolment;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.repository.CourseRepository;
import dev.enkay.student_service.repository.EnrolmentRepository;
import dev.enkay.student_service.repository.StudentRepository;
import dev.enkay.student_service.service.EnrolmentService;
import dev.enkay.student_service.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrolmentServiceImpl implements EnrolmentService {

  private final CourseRepository courseRepository;
  private final EnrolmentRepository enrolmentRepository;
  private final StudentRepository studentRepository;
  private final UserService userService;

  public EnrolmentServiceImpl(CourseRepository courseRepository, EnrolmentRepository enrolmentRepository,
    StudentRepository studentRepository, UserService userService) {
    this.courseRepository = courseRepository;
    this.enrolmentRepository = enrolmentRepository;
    this.studentRepository = studentRepository;
    this.userService = userService;
  }

  @Override
  @Transactional
  public EnrolmentResponse enrolInCourse(EnrolmentRequest request) {
    var user = userService.getCurrentUser();

    // Get or create student profile
    var student = studentRepository.findByUser(user)
        .orElseGet(() -> {
          Student newStudent = new Student();
          newStudent.setUser(user);
          return studentRepository.save(newStudent);
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

    return new EnrolmentResponse(
        savedEnrolment.getId(),
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
            new CourseInfoInEnrolment(
                e.getCourse().getId(),
                e.getCourse().getCode(),
                e.getCourse().getTitle(),
                e.getCourse().getDescription())))
        .collect(Collectors.toList());
  }
}
