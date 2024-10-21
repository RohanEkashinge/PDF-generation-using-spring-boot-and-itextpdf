# PDF Generator Service

## Overview
The PDF Generator Service is a Spring Boot application designed to generate and store PDF documents based on user input. This service provides a RESTful API that allows clients to create PDF invoices and store them in a database. The application is built using Java, Spring Boot, and iText for PDF generation.

## Features
- Generate PDF invoices from user-defined data.
- Store generated PDFs in a MySQL database.
- Retrieve existing PDFs based on data hash.
- Comprehensive JUnit tests for the service layer.

## Getting Started

### Prerequisites
- Java 17 or later
- Maven
- MySQL Database

### Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/RohanEkashinge/PDF-generation-using-spring-boot-and-itextpdf.git
    cd pdf-generator
    ```

2. Set up your database:
   - Create a MySQL database named `userDatabase`.
   - Adjust the database name, username, and password as per your configuration.

3. Environment Configuration: You can use a `.env` file to set your database configuration:
   - Create a `.env` file in the root directory and add the following fields:
     ```
     SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/userDatabase
     SPRING_DATASOURCE_USERNAME=username
     SPRING_DATASOURCE_PASSWORD=password
     ```
   - Alternatively, you can comment out the following lines in `PdfGeneratorApplication.java`:
     ```java
     System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
     System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
     System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
     ```
   - And directly configure your settings in `src/main/resources/application.properties`.

4. Build the application: Run the following command to build the project:
    ```bash
    mvn clean install
    ```

5. Run the application: You can start the application using:
    ```bash
    mvn spring-boot:run
    ```

## API Usage
The API provides endpoints to generate and manage PDFs. Below are the main endpoints available:

### Generate PDF
- **POST** `/api/pdf/generate`

#### Request Body:
```json
{
  "seller": "XYZ Pvt. Ltd.",
  "sellerGstin": "29AABBCCDD121ZD",
  "sellerAddress": "New Delhi, India",
  "buyer": "Vedant Computers",
  "buyerGstin": "29AABBCCDD131ZD",
  "buyerAddress": "New Delhi, India",
  "items": [
    {
      "name": "Product 1",
      "quantity": "12 Nos",
      "rate": 123.00,
      "amount": 1476.00
    }
  ]
}
```

**Response**:
The generated PDF document in byte format.

## API Documentation
You can find the API documentation in the file 
```bash docs/pdf-generator-api.postman_collection.json ```
 This collection can be imported directly into Postman for testing the API endpoints.

## Testing
JUnit tests have been implemented for the service layer to ensure reliability and maintainability. The test class is located at:

```bash
src/test/java/com/example/pdf_generator/service/PdfServiceTest.java
```

You can run the tests using:

```bash
mvn test
```

## Test Coverage
The tests cover:

- Generation of new PDFs.
- Retrieval of existing PDFs.
- Validation of input data and handling of edge cases.

This API provides a robust framework for generating PDF. Follow the instructions above to set up your environment, run the application, and test the API endpoints effectively.
