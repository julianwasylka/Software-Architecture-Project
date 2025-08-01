# Git branching
The git branching model used was [Git Flow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow). It defines a set of standard branches and workflows for managing development and releases. It is a popular choice for software development teams because it provides a clear and structured way to collaborate and maintain code quality. Some of the advantages of using Git Flow are:
- **Improved collaboration:** Git Flow promotes collaboration among team members by defining clear roles and responsibilities for each branch. 
- **Better code quality:** By separating development work into feature branches, Git Flow helps to ensure that code is thoroughly tested and reviewed before it is merged into the main branch. 
- **Easier releases:** Git Flow makes it easier to manage releases by providing a clear path from development to production. 
- **Reduced risk:** Git Flow helps to reduce the risk of introducing bugs into production by providing a way to isolate changes and test them thoroughly before merging them into the main branch.

--- 

---

# Commits methodology
In order to allow an easy analysis of the work developed, the following commit methodology suffixes were adopted:

- `Add:` --> Created something new in the repository
- `Update:` --> Updated something that already existed
- `Delete:` --> Deleted something that already existed

--- 

---

# Work Division
| Task                                                       | Implementer     | Done       |
|------------------------------------------------------------|-----------------|------------|
| Docs - ADD, Technical Memos and Quality Attribute Scenario | Miguel & Julian | Yes        |
| Docs - UML Diagrams                                        | Miguel          | Yes        |
| Business domain segregation                                | Miguel          | Yes        |
| Messaging and Domain Events                                | Miguel          | Yes        |
| Tests - Performance tests                                  | Miguel          | Yes        |
| Tests - Isolated tests from MB and DBMS                    | Julian          | Incomplete |
| Tests - CDC                                                | Julian & Miguel | Incomplete |
| CQRS                                                       | Julian & Miguel | No         |
| Polyglot persistence                                       | Julian & Miguel | No         |
| Statistics Microservice                                    | Julian & Miguel | No         |
| Suggestions Microservice                                   | Julian          | Yes        |
| Suggesting New Book Acquisitions                           | Julian          | Yes        |
| Recommending Returned Books                                | Miguel          | Yes        |

<br>

---

---

# Project Documentation - Transition to Microservices

## ADD - Overview of Requirements

### Functional Requirements
- Enable users to suggest new books for acquisition.
- Allow users to provide feedback on returned books.

### Design Requirements
- Business domain segregation: the monolithic application must be segregated in (at least) three distinct but collaborating applications (e.g. Books, Readers and Lendings)
- Cloning: Multiple instances of each of the previous application must be deployed in Virtual Machines or containers.

### Quality Requirements
- Ensure high scalability and maintainability by decoupling services.
- Achieve seamless integration between microservices.
- Maintain data consistency across services.

---

## Functional Requirement 1 - Suggesting New Book Acquisitions

### Quality Attributes Table

| Attribute            | Description                                                               |
|----------------------|---------------------------------------------------------------------------|
| **Stimulus**         | Users want to suggest books for acquisition.                              |
| **Stimulus Source**  | Readers requesting the ability to influence library acquisitions.         |
| **Environment**      | Readers interact with the system through the Suggestions microservice.    |
| **Artifact**         | Suggestions Microservice.                                                 |
| **Response**         | Allow users to submit suggestions for new books via the system interface. |
| **Response Measure** | Suggestions are stored and available for library staff to review.         |

### Technical Memo for Suggesting New Books

- **Issue**: No mechanism for readers to suggest books.
- **Problem**: Limited user engagement and input.
- **Summary of Solution**: Implement a feature within the Suggestions microservice to allow book suggestions.
- **Factors**: User experience and suggestion management.
- **Solution**:
    - Add a submission form for users to suggest books.
    - Store suggestions in a database.
- **Motivation**: Enhances user satisfaction and library relevance.
- **Alternatives**: Manual suggestions through email or forms.

---

## Functional Requirement 2 - Recommending Returned Books

### Quality Attributes Table

| Attribute            | Description                                                                               |
|----------------------|-------------------------------------------------------------------------------------------|
| **Stimulus**         | Readers want to provide feedback on books they have borrowed.                             |
| **Stimulus Source**  | Readers indicating preferences for future recommendations.                                |
| **Environment**      | Readers interact with the system through the Lendings microservice upon returning a book. |
| **Artifact**         | Lendings Microservice.                                                                    |
| **Response**         | Allow readers to rate returned books positively or negatively.                            |
| **Response Measure** | Ratings are stored and can influence future recommendations.                              |

### Technical Memo for Recommending Returned Books

- **Issue**: Lack of feedback on borrowed books.
- **Problem**: Limited data for generating meaningful book recommendations.
- **Summary of Solution**: Add a feedback feature within the Lendings microservice.
- **Factors**: User engagement and recommendation quality.
- **Solution**:
    - Provide an option to rate books upon return.
    - Store ratings in a database for analysis.
    - Incorporate ratings into recommendation algorithms.
- **Motivation**: Improves user satisfaction and library recommendations.
- **Alternatives**: No rating system or manual feedback collection.
- **Pending Issues**: Design an algorithm to process and utilize the ratings effectively.

--- 

## Design Requirement 1 - Business domain segregation

### Quality Attributes Table

| Attribute            | Description                                                                                                  |
|----------------------|--------------------------------------------------------------------------------------------------------------|
| **Stimulus**         | Difficulty in scaling and maintaining a monolithic architecture as the project grows.                        |
| **Stimulus Source**  | Product owner and development team recognizing the need for scalability and flexibility.                     |
| **Environment**      | The system currently operates as a monolith.                                                                 |
| **Artifact**         | LMS software.                                                                                                |
| **Response**         | The system will be divided into multiple microservices, each handling a distinct domain of responsibility.   |
| **Response Measure** | Successful decomposition of the monolithic system into functional microservices with independent deployment. |

### Technical Memo for Transition to Microservices

- **Issue**: The current monolithic system limits scalability, flexibility, and independent feature deployment.
- **Problem**: High coupling in the monolithic architecture makes it challenging to introduce new features or scale specific components.
- **Summary of Solution**: Decompose the monolithic application into independent microservices, each responsible for a distinct domain.
- **Factors**: Scalability, modularity, and maintainability.
- **Solution**:
    - Identify domains within the current system.
    - Design microservices for Library, Users, Lendings, Suggestions, and Statistics.
    - Implement communication between services using a message broker.
- **Motivation**: Transitioning to microservices improves scalability and allows independent development and deployment.
- **Alternatives**:
    - **Option 1**: Retain the monolithic structure.
        - **Pros**: No upfront architectural change required.
        - **Cons**: Scalability and maintainability issues persist.
    - **Option 2**: Adopt a modular monolith approach.
        - **Pros**: Easier to refactor compared to microservices.
        - **Cons**: Limited scalability benefits.
- **Pending Issues**: Ensure that data consistency and inter-service communication are handled efficiently (Resolved using RabbitMQ and AMQP).

---

## Design Requirement 1.1 - Proposed Microservice Division

### Quality Attributes Table

| Attribute            | Description                                                                                 |
|----------------------|---------------------------------------------------------------------------------------------|
| **Stimulus**         | Need for clearly defined responsibilities for each service.                                 |
| **Stimulus Source**  | Development team’s analysis of system domains.                                              |
| **Environment**      | Existing system to be decomposed into five microservices.                                   |
| **Artifact**         | LMS software.                                                                               |
| **Response**         | Define and implement five microservices: Library, Users, Lendings, Suggestions, Statistics. |
| **Response Measure** | Each microservice independently handles its responsibilities and integrates seamlessly.     |

### Technical Memo for Microservice Division

- **Issue**: The monolithic application combines all responsibilities, causing complexity.
- **Problem**: High interdependence between components.
- **Summary of Solution**: Divide the monolith into five functional microservices.
- **Factors**: Domain separation, scalability, and data management.
- **Solution**:
    - **Library Microservice**: Manages books, authors, genres and book recommendations.
    - **Users Microservice**: Handles user accounts, authentication and readers details.
    - **Lendings Microservice**: Manages book lending operations.
    - **Suggestions Microservice**: Allows users to suggest new book acquisitions.
    - **Statistics Microservice**: Provides insights and statistics.
- **Motivation**: Clear domain boundaries reduce complexity and enhance scalability.
- **Alternatives**: No division or a different partitioning scheme.
- **Pending Issues**: Determine how data will be synchronized across services (Resolved using RabbitMQ and AMQP).

---

## Design Requirement 2 - Cloning

### Quality Attributes Table

| Attribute            | Description                                                                                                                              |
|----------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| **Stimulus**         | The need to deploy multiple instances of each microservice to ensure scalability and fault tolerance.                                    |
| **Stimulus Source**  | Project requirements and DevOps strategy emphasizing containerized deployment for scalability and flexibility.                           |
| **Environment**      | The microservices-based system running in a Dockerized environment with each service and its database in a container.                    |
| **Artifact**         | The microservices: Library, Lendings, Users, Suggestions, and Statistics, with their respective Command and Query modules.               |
| **Response**         | Each microservice (Command and Query) will be deployed as independent containers, each with its own database container.                  |
| **Response Measure** | Successful deployment of multiple instances of each microservice and its database, verified by load testing and availability monitoring. |

### Technical Memo for Transition to Microservices

- **Issue**: The system needs to ensure scalability and fault tolerance by running multiple instances of each microservice in a containerized environment.
- **Problem**: Running a single instance of each microservice limits the system's ability to handle high traffic and introduces a single point of failure.
- **Summary of Solution**: Deploy each microservice (Command and Query) in a separate Docker container, along with its dedicated database in another container, enabling multiple instances for scalability and resilience.
- **Factors**: Scalability, fault tolerance, independent deployment, and simplified infrastructure management.
- **Solution**:
  - Use Docker Compose to orchestrate the deployment of containers for microservices and their respective databases.
  - Ensure each microservice (e.g., LendingsCommand and LendingsQuery) runs in isolated containers, communicating over the network.
  - Implement a container orchestration tool like Kubernetes or Docker Swarm for scaling and managing multiple instances.
  - Ensure databases are independently containerized and configured for high availability and persistence.
- **Motivation**: Running microservices and their databases in isolated containers ensures scalability, fault tolerance, and ease of replication in different environments.
- **Alternatives**:
    - **Option 1**: Deploy microservices without containerization.
        - **Pros**: Avoids the complexity of container orchestration.
        - **Cons**: Limited portability, scalability, and resource isolation.
    - **Option 2**: Deploy microservices in virtual machines instead of containers.
        - **Pros**: Greater resource allocation and isolation.
        - **Cons**: Higher overhead compared to lightweight containers.
- **Pending Issues**: Ensure inter-service communication, logging, and monitoring are optimized across multiple instances (resolved using RabbitMQ and centralized logging).

---

## Patterns adoption

The following architectural patterns were adopted in the design and implementation of the application to ensure scalability, maintainability, and proper domain-driven design principles:

**Strangler Fig Pattern**
- The Strangler Fig Pattern was used to incrementally migrate the monolithic application into a microservices architecture. Existing functionality was gradually replaced with new microservices without disrupting ongoing operations. This approach allowed for seamless migration by routing requests to either the monolith or the new microservices based on the feature set.

**Command-Query Responsibility Segregation (CQRS)**
- The CQRS pattern was implemented by splitting each domain into two distinct services: one for handling commands (write operations) and another for handling queries (read operations). For example, the Lendings microservice was divided into LendingsCommand and LendingsQuery, each with its own logic and database. This separation optimizes the system for scalability and ensures that read and write concerns are addressed independently.

**Database-per-Service**
- Each microservice operates with its own dedicated database under the Database-per-Service pattern. For instance, LendingsCommand and LendingsQuery have separate databases to ensure data ownership and isolation. This allows independent scaling of services and minimizes coupling between microservices.

**Polyglot Persistence**
- To optimize each microservice’s functionality, the application adopted Polyglot Persistence, using different database technologies based on the use case. For example, a relational database like PostgreSQL is used for transactional services, while a document database like MongoDB is used for services requiring unstructured data storage.

**Messaging**
- The application uses asynchronous messaging to facilitate communication between microservices. This decouples services, ensuring loose coupling and fault tolerance. RabbitMQ is used as the AMQP message broker, enabling reliable message delivery and event-driven communication across services.

**Domain Events**
- Domain Events are used to capture significant occurrences within the system. For instance, when a book is created or updated in the LibraryCommand service, a corresponding domain event is published to notify other services (e.g., StatisticsCommand or LendingsCommand). This ensures consistency across the system while maintaining service independence.

**Saga**
- The Saga pattern was implemented to handle distributed transactions across multiple microservices. For example, in the lending process, when returning a book with a recommendation the LendingsCommand microservice coordinates with other services like LibraryCommand to ensure the transaction completes successfully or rolls back in case of failure. This was achieved using a choreographed approach, where services communicate through domain events.

---

---

# Implemented Examples

## Running locally with in-memory database
### Create Reader

- ![Sender](Images/Implementation%20Examples/Sender%20-%20Users%20-%20Create%20User%20and%20Reader%20Details.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Lendings%20-%20Create%20User%20and%20Reader%20Details.png)

---

### Update Reader

- ![Sender](Images/Implementation%20Examples/Sender%20-%20Update%20Reader%20Details.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Lendings%20-%20Update%20Reader%20Details.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Users%20-%20Update%20Reader%20Details.png)

<br>

- ![DB](Images/Implementation%20Examples/DB-%20Update%20Reader%20Details.png)

---

### Create Book

- ![Postman](Images/Implementation%20Examples/Postman%20-%20Create%20Book.png)

<br>

- ![Sender](Images/Implementation%20Examples/Sender%20-%20Library%20-%20Create%20Book.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Lendings%20-%20Create%20Book.png)

---

### Book Returned Successfully

- ![Postman](Images/Implementation%20Examples/Postman%20-%20Return%20book.png)

<br>

- ![Sender](Images/Implementation%20Examples/Sender%20-%20Lendings%20-%20Return%20Book.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Library%20-%20Return%20Book.png)

<br>

- ![DB](Images/Implementation%20Examples/DB%20-%20%20Recommendation%20Created.png)

<br>

- ![DB](Images/Implementation%20Examples/DB%20-%20Book%20Returned.png)

---

### Book Returned Failure

- ![CODE](Images/Implementation%20Examples/CODE%20-%20Forcing%20failure%20in%20lending%20returned%20event.png)

<br>

- ![Sender](Images/Implementation%20Examples/Sender%20-%20Lendings%20-%20Return%20Book%20Failure.png)

<br>

- ![Receiver](Images/Implementation%20Examples/Receiver%20-%20Library%20-%20Recommendation%20Creation%20Failure.png)

---

## Running locally with docker cloning

- ![Docker Script](Images/Docker/Docker%20script.png)

<br>

- ![Docker Instances](Images/Docker/Docker%20-%20Instances%20Running.png)

### Create Book

- ![Postman](Images/Docker/Create%20Book%20-%20Postman.png)

<br>

- ![Sender](Images/Docker/Book%20MS%20that%20sent%20the%20book%20created%20event.png)

<br>

- ![Receiver](Images/Docker/Book%20MS%20that%20received%20the%20book%20created%20event.png)

<br>

- ![DB1](Images/Docker/Create%20Book%20-%20DB1.png)

<br>

- ![DB2](Images/Docker/Create%20Book%20-%20DB2.png)


### Update Book

- ![Postman](Images/Docker/Update%20Book%20-%20Postman.png)

<br>

- ![Sender](Images/Docker/Book%20MS%20that%20sent%20the%20book%20updated%20event.png)

<br>

- ![Receiver](Images/Docker/Book%20MS%20that%20received%20the%20book%20updated%20event.png)

<br>

- ![DB1](Images/Docker/Update%20Book%20-%20DB1.png)

<br>

- ![DB2](Images/Docker/Update%20Book%20-%20DB2.png)
