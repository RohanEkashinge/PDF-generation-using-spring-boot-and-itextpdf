package com.example.pdf_generator.controller;


import com.example.pdf_generator.dto.PdfRequestDTO;
import com.example.pdf_generator.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PdfController handles HTTP requests related to PDF generation.
 */
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);

    @Autowired
    private PdfService pdfService;

    /**
     * Generates a PDF based on the input data provided in the request body.
     *
     * @param inputData the data required to generate the PDF
     * @return a ResponseEntity containing the generated PDF as a byte array
     * @throws Exception if there is an error during PDF generation
     */
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody PdfRequestDTO inputData) throws Exception {
        logger.info("Received request to generate PDF with data: {}", inputData);
        byte[] pdfContent = pdfService.generateAndStorePdf(inputData);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice.pdf");

        logger.info("PDF generated successfully, returning response.");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfContent);
    }
}