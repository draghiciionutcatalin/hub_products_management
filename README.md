## Hub Products Management

A Spring Boot application with Maven and Java 17 that provides a RESTful API.\
This is a project for managing products and prices.

## Main features

- Get product by id
- Add new product
- Update product
- Delete product
- Basic Authentication with Role based access control
- Logging
- Exception handler
- Unit Testing

## Requirements

- Java 17 JDK
- Maven
- Git
- any IDE

## Setup

1. Clone Repository
    ```sh
    git clone https://github.com/draghiciionutcatalin/hub_products_management.git
    ```
2. Open the project in your IDE
3. Build the project
    ```sh
    mvn clean install
   ```
4. Run the app
    ```sh
   mvn spring-boot:run
   ```
5. Test the available APIs with your preferred method or the included Swagger UI

6. Run test coverage
   ```sh
   mvn clean test jacoco:report
   ```

7. Open the test coverage report in folder `/target/site/jacoco/index.html`

## Swagger API Documentation

After the application has started, please access the Swagger UI to view and interact with the API documentation:

- In a browser navigate to `http://localhost:8080/swagger-ui/index.html`

## Authentication

#### Admin Role

- **Username**: admin **Password**: admin

#### User Role

- **Username**: user **Password**: user