# 🎓 Student Management Services Repository

This repository contains a suite of backend services for managing student-related activities, built with **Spring Boot**, **PostgreSQL**, **Flyway**, and **Docker Compose**.

> ⚡ Note: This is a **multi-service repository** (Monorepo). Each service lives in its own directory.

---

## 🚀 Current Services

- [`student-service`](./student_service/): Handles authentication, student profiles, courses, and enrolments.

---

## 📦 Tech Stack Overview

- Java 21
- Spring Boot 3.3.x
- Spring Security (JWT based)
- Spring Data JPA
- PostgreSQL 14
- Flyway
- Docker Compose
- Swagger UI for API Documentation

---

## 🛠️ Local Setup (for all services)

Each service contains its own setup instructions inside its folder.  
Docker Compose helps to orchestrate shared resources like databases.

---

## 📚 Roadmap

- [x] `student-service` (Registration, Courses, Enrolments)
- [ ] `finance-service` (Invoices, Payments, Graduation Eligibility)
- [ ] `library-service` (Books)

---

## 📄 License

This project is part of an MSc Course Assessment and not intended for production use without further enhancements.
