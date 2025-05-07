# Java Advanced: Backend Services course

## Tech stack:
* Java 24
* Spring Boot 3.5.0-SNAPSHOT

## Notes:
* At the following path you can find a video presentation of the task "src/main/java/com/kdob/jabs/task{#of the task}/result"

## Module #1 - Spring Boot

### Task 1 - Hello-world application
* Use Spring Initializr to create a basic Spring Boot application with minimal dependencies.
* Implement the CommandLineRunner interface in your application.
* Output the message hello world to the console.
* Run the application and verify that the message is displayed when the Spring context starts.

### Task 2 - CRUD REST application
* Choose a simple entity (e.g., Customer, Product) with a few attributes (e.g., id, name, etc.).
* Use Spring Data JPA for repository management, but avoid the use of Spring Data REST Starter.
* Create a REST controller with endpoints supporting Create, Read, Update, and Delete (CRUD) operations for the entity.
* Test the endpoints using tools such as Postman or cURL.

### Task 3 - CRUD application: security
* Implement authentication and authorization mechanisms using OAuth2 and JWT tokens.
* Configure a security setup that includes OAuth2 for user authentication.
* Issue JWT tokens for authenticated users, and ensure incoming requests validate these tokens.
* Restrict access to endpoints based on user roles (e.g., USER, ADMIN).

### Task 4 - CRUD application: externalized configuration
* Use Spring Profiles to support multiple environments like local, dev, stg, prod.
* Create separate configuration files (application-{profile}.yml) for each environment with different database properties.
* Enable the correct profile during runtime (e.g., by setting spring.profiles.active), and ensure corresponding configuration is applied.

### Task 5 - CRUD application: data migrating
* Select a tool like Flyway or Liquibase for managing database schema migrations.
* Provide migration scripts or files to create and update the database schema as needed.
* Configure the migration tool to run automatically during the application startup.

### Task 6 - Actuator
* Enable Spring Boot Actuator within your application for monitoring and management purposes.
* Implement custom health indicators to monitor application-specific health metrics.
* Add custom metrics for Prometheus or other tools to observe the application's behavior.

### Task 7 - CRUD application: testing
* Configure an in-memory database (e.g., H2) for test purposes.
* Implement repository tests to validate data access logic.
* Write unit tests for service-level logic.
* Test RestController endpoints using MockMvc.
* Perform integration tests to validate end-to-end behavior in the application.
