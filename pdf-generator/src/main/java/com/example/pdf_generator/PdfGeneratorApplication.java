package com.example.pdf_generator;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the PDF Generator API.
 */
@SpringBootApplication
public class PdfGeneratorApplication {



	public static void main(String[] args) {
		// Load the .env file from the root directory
		Dotenv dotenv = Dotenv.configure()
				.directory("path to .env file")  // Full path to the project root
				.load();

		System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
		System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
		System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
		SpringApplication.run(PdfGeneratorApplication.class, args);
	}

}
