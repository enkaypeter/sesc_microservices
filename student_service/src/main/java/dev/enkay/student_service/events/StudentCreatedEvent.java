package dev.enkay.student_service.events;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreatedEvent implements Serializable {
  private Long studentId;
  private String fullName;
  private String email;
}
