# Multi-Datasource Spring Boot Example

This project is a demo of how to work with multiple databases in a single Spring Boot application. It connects to:

- **PostgreSQL** – for regular transactional data like orders
- **MongoDB** – to store document-based product data
- **H2 (in-memory)** – for temporary or analytical data (like reports or logs)

It's a practical use case for systems that need to pull from different types of data sources for different purposes.

---

## What's Inside?

The project is set up with:

- JPA for PostgreSQL and H2
- Spring Data MongoDB
- Separate configuration classes for each datasource
- Swagger UI to test APIs
- H2 Console enabled for quick inspection

---

## How Each DB Is Set Up

### PostgreSQL (Primary Relational DB)

Used for your main application data like orders or users.

```properties
postgres.datasource.jdbc-url=jdbc:postgresql://localhost:5432/orderdb
postgres.datasource.username=postgres
postgres.datasource.password=root
postgres.jpa.hibernate.ddl-auto=update
```

### MongoDB (NoSQL)
Used to store flexible data like product info, which can vary between records.

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=productdb
spring.data.mongodb.auto-index-creation=true
```

### H2 (In-Memory)
Used for temporary things like analytics or logs that are quite immutable.

```properties
h2.datasource.jdbc-url=jdbc:h2:mem:analyticsdb;DB_CLOSE_DELAY=-1
h2.datasource.username=sa
h2.datasource.password=
h2.datasource.driver-class-name=org.h2.Driver
h2.jpa.hibernate.ddl-auto=create-drop
h2.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
h2.jpa.show-sql=false

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

** `create-drop` is fine for development but avoid it in production.

## Running the App

Make sure PostgreSQL and MongoDB are up and running locally. Then just:

```java
./mvnw spring-boot:run
```
You can access:
- Swagger UI at: http://localhost:8080/swagger-ui.html
- H2 Console at: http://localhost:8080/h2-console


## Key Concepts
### 1. Separate Config Classes
Each database is configured in its own class – we define the data source, entity manager, and transaction manager separately.

```java
@EnableJpaRepositories(
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager",
    basePackages = {"com.deb.multi_datasource.repository.postgres"}
)
public class PostgresConfig { ... }
```
### 2. Property Separation
Only one datasource (usually the primary) uses the spring.datasource.* properties. For others, you need to define your own custom prefixes (like h2.datasource.*, postgres.datasource.*) and bind them using @ConfigurationProperties. In this project, I used separate configuration for each datasource to explicitly override the default datasource configuration.

### 3. MongoDB Integration
Mongo is handled by Spring Data MongoDB – no need for EntityManager or transactions here. Just define your model with @Document.

---


## Why Use Multiple Databases?

There are real-world scenarios where using more than one type of database makes sense:

| Use Case                             | Recommended DB |
| ------------------------------------ | -------------- |
| Orders, users, payments (structured) | PostgreSQL     |
| Product catalog, flexible schema     | MongoDB        |
| Temp reports, in-memory analysis     | H2 or Redis    |


## Contributing

This is just a demo/learning project. Feel free to fork, extend, or contribute if you're interested!
