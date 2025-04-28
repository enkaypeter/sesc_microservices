package dev.enkay.student_service.events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StudentEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final String studentExchange;
  private final String studentRoutingKey;

  public StudentEventPublisher(RabbitTemplate rabbitTemplate,
    @Value("${rabbitmq.student.exchange}") String studentExchange,
    @Value("${rabbitmq.student.routing-key}") String studentRoutingKey) {
      this.rabbitTemplate = rabbitTemplate;
      this.studentExchange = studentExchange;
      this.studentRoutingKey = studentRoutingKey;
  }

  public void publishStudentCreated(StudentCreatedEvent event) {
    rabbitTemplate.convertAndSend(studentExchange, studentRoutingKey, event);
  }
}
