# 🎓 Student Management Services

This repository contains a suite of backend services for managing student-related activities in an academic context, implemented as part of an Msc course assessment. 

> ⚡ Note: This is a **multi-service repository**. Each service lives in its own directory.

---

## 🚀 Services

| Service Directory  | Description                                                                                | Technology                                                                                  |
| ------------------ | ------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------- |
| `student_service/` | Authentication (JWT), Student profiles, Courses listing, Enrolment, Graduation eligibility | Java 21, Spring Boot 3.3.x, Spring Security, Spring Data JPA, PostgreSQL 14, Flyway, Docker |
| `finance_service/` | Invoice creation, status tracking, payments                                                | Java 17, Spring Boot, Spring Data JPA, MariaDB 10.6, Docker                                 |
| `library_service/` | Library account creation, book returns, fines                                   | Python 3.7, Flask, MariaDB 10.6, Docker                                                     |

---

## 📦 Infrastructure
![network_infra_diagram](https://res.cloudinary.com/dc9klxnmy/image/upload/v1746725502/net_infra_diagram_d8uqab.png)

We orchestrate all services and shared infrastructure using two `docker compose` files:

* **Infra stack**: `docker-compose/docker-compose.infra.yml`

  * Postgres (Student DB)
  * MariaDB (Finance DB)
  * MariaDB (Library DB)
  * RabbitMQ (*Pub/Sub)
  * Consul (Service Discovery)
  * Traefik (Proxy)

* **App stack**: `docker-compose/docker-compose.app.yml`

  * API Gateway (Spring Cloud Gateway)
  * Student Service **[student-service]**
  * Finance Service **[finance-service]**
  * Library Service **[library-service]**

> All compose services share the `sesc_infra_net` and `sesc_app_net` networks.

### Prerequisites

* Docker & Docker Compose (v2+)
* Java 21 (for student-service)
* Python 3.7+ (for library-service development)

---

## 🛠️ Local Setup

1. **Clone and navigate**

   ```bash
   git clone https://github.com/enkaypeter/sesc_microservices.git
   ```

2. **Build Entire Stack**

   ```bash
   make build
   ```

3. **Start Infrastructure**

   ```bash
   make infra
   ```

4. **Start Application Services**

    ```bash
    make app
   ```

5. **Verify**

   * student-service API: [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)
   * finance-service API: [http://localhost:8081](http://localhost:8081)
   * library-service API: [http://localhost:8082](http://localhost:8082)

5. **Shutdown**

   ```bash
   make down-v
   ```

---


## 📚 Next Steps

* [ ] **Event-Driven Enhancements:**

  * Publish `StudentCreatedEvent` and `InvoiceCreatedEvent` via RabbitMQ.
  * Implement subscribers in Finance and Library for eventual consistency.


* [ ] **Observability & Resilience:**
  * Expose Prometheus metrics and add Grafana dashboards.
  * Enable distributed tracing with OpenTelemetry/Jaeger.

* [ ] **CI/CD & Deployment:**

  * Add GitHub Actions (or equivalent) to build, test, and push Docker images.


* [ ] **Developer Experience & Front-End:**

  * Add a front-end that consumes the Student API.
  * Improve local dev with a `Makefile` or `bash` script to spin up all services.

---

## 📄 License

*This project is part of an MSc Course Assessment and not intended for production use without further enhancements.*
