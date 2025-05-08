package dev.enkay.student_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public TopicExchange studentExchange() {
    return new TopicExchange("student.exchange");
  }

  @Bean
  public Queue studentQueue() {
    return new Queue("student.created.queue");
  }

  @Bean
  public Binding binding(Queue studentQueue, TopicExchange studentExchange) {
    return BindingBuilder.bind(studentQueue).to(studentExchange).with("student.created");
  }
}
