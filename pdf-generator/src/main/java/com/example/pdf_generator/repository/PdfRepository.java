package com.example.pdf_generator.repository;

import com.example.pdf_generator.model.PdfDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfRepository extends JpaRepository<PdfDocument, Long> {
    Optional<PdfDocument> findByDataHash(String dataHash);


}
