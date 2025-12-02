# Distributed Library Management System

## 📌 Repository Overview & Navigation

This repository contains the **source code** and business logic for the microservices-based Library Management System.

> ⚠️ **Important:**
> * **Documentation:** Detailed architectural documentation, diagrams, and technical memos are located in the local folder: [`/library_microservice`](./library_microservice).
> * **DevOps & Infrastructure:** The CI/CD configuration (Jenkins), Docker Swarm setup, and infrastructure code are hosted in a separate repository:
>     👉 **[Software_Organization_Project](https://github.com/julianwasylka/Software_Organization_Project)**

---

## 📖 Project Description

This project demonstrates the transition from a monolithic architecture to a distributed **Microservices Architecture** using the **Strangler Fig Pattern**. The system is designed to handle high scalability and maintainability by segregating business domains into independent services.

The system is composed of several collaborating microservices:
* **Library Service:** Manages books, authors, and inventory.
* **Users Service:** Handles authentication and reader details.
* **Lendings Service:** Manages the borrowing and returning process.
* **Suggestions Service:** Allows users to propose new book acquisitions.
* **Statistics Service:** Provides insights and reporting.

## 🛠 Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot (Web, Data JPA)
* **Communication:** RabbitMQ (AMQP) for event-driven communication
* **Databases (Polyglot Persistence):**
    * PostgreSQL (Transactional data)
    * MongoDB (Read models / Unstructured data)
* **Testing:** JUnit, Mockito
* **Architecture Patterns:** DDD, CQRS, Event Sourcing

## 🏗 Architecture Highlights

* **Domain-Driven Design (DDD):** Clear separation of bounded contexts (Library, Lendings, Users).
* **Event-Driven Architecture:** Services communicate asynchronously via **RabbitMQ** to ensure loose coupling.
* **Polyglot Persistence:** Each service uses the database technology best suited for its needs (Database-per-Service pattern).
* **Resilience:** Implementation of patterns to handle distributed transaction failures (Saga Pattern).
