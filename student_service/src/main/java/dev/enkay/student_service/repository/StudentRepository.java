package dev.enkay.student_service.repository;

import dev.enkay.student_service.entity.Student;
import dev.enkay.student_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
  Optional<Student> findByUser(User user);
}