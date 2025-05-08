package dev.enkay.student_service.service.impl;

import dev.enkay.student_service.dto.profile.ProfileResponse;
import dev.enkay.student_service.dto.profile.UpdateProfileRequest;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.service.ProfileService;
import dev.enkay.student_service.service.UserService;
import dev.enkay.student_service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

  private final StudentRepository studentRepository;
  private final UserService userService;

  @Autowired
  public ProfileServiceImpl(StudentRepository studentRepository, UserService userService) {
    this.studentRepository = studentRepository;
    this.userService = userService;
  }

  @Override
  public ProfileResponse getProfile() {
    var user = userService.getCurrentUser();

    var student = studentRepository.findByUser(user)
      .orElseThrow(() -> new IllegalArgumentException("Student profile not found."));

    return new ProfileResponse(
      student.getId(),
      student.getStudentId(),
      user.getEmail(),
      student.getFirstName(),
      student.getLastName());
  }

  @Override
  public ProfileResponse updateProfile(UpdateProfileRequest request) {
    var user = userService.getCurrentUser();

    var student = studentRepository.findByUser(user)
      .orElseThrow(() -> new IllegalArgumentException("Student profile not found."));

    student.setFirstName(request.getFirstName());
    student.setLastName(request.getLastName());

    var updatedStudent = studentRepository.save(student);

    return new ProfileResponse(
      updatedStudent.getId(),
      student.getStudentId(),
      user.getEmail(),
      updatedStudent.getFirstName(),
      updatedStudent.getLastName());
  }
}
