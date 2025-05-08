package dev.enkay.student_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class StudentServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
			.ignoreIfMissing()
			.load();
		dotenv.entries()
			.forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(StudentServiceApplication.class, args);
	}

}
