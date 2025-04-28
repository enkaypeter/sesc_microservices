package dev.enkay.student_service.events;

import dev.enkay.student_service.events.StudentCreatedEvent;
import dev.enkay.student_service.events.StudentEventPublisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StudentEventPublisherTest {

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private StudentEventPublisher publisher;

  @BeforeEach
  void setUp() {
    publisher = new StudentEventPublisher(rabbitTemplate, "student.exchange", "student.created");
  }

  @Test
  void shouldPublishStudentCreatedEventSuccessfully() {
    // Arrange
    StudentCreatedEvent event = new StudentCreatedEvent(1L, "James John", "test@example.com");

    // Act
    publisher.publishStudentCreated(event);

    // Assert
    verify(rabbitTemplate, times(1))
      .convertAndSend("student.exchange", "student.created", event);
  }
}
