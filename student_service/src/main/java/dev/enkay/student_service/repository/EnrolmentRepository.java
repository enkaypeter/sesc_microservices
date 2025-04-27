package dev.enkay.student_service.repository;

import dev.enkay.student_service.entity.Enrolment;
import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
  List<Enrolment> findByStudent(Student student);
  Optional<Enrolment> findByStudentAndCourse(Student student, Course course);
}
