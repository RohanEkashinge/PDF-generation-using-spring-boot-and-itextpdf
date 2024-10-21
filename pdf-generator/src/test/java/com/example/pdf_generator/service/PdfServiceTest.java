package com.example.pdf_generator.service;


import com.example.pdf_generator.dto.PdfRequestDTO;
import com.example.pdf_generator.model.PdfDocument;
import com.example.pdf_generator.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfServiceTest {

    private PdfService pdfService;
    private PdfRepository pdfRepository;

    @BeforeEach
    public void setUp() {
        // Mocking the PdfRepository to isolate tests
        pdfRepository = mock(PdfRepository.class);
        pdfService = new PdfService();
        pdfService.setPdfRepository(pdfRepository); // Assuming a setter is present for testing
    }

    @Test
    public void testGenerateAndStorePdf_WhenPdfExists_ReturnsExistingPdf() throws Exception {
        // Arrange
        PdfRequestDTO inputData = createSampleInputData();
        String dataHash = hashInputData(inputData); // Generate hash for the input data

        // Mock the repository to return an existing PDF document
        PdfDocument existingPdf = new PdfDocument(dataHash, new byte[]{1, 2, 3});
        when(pdfRepository.findByDataHash(dataHash)).thenReturn(Optional.of(existingPdf));

        // Act
        byte[] pdfContent = pdfService.generateAndStorePdf(inputData);

        // Assert
        assertArrayEquals(existingPdf.getPdfContent(), pdfContent, "The returned PDF content should match the existing PDF content.");
        verify(pdfRepository, times(1)).findByDataHash(dataHash); // Verify repository interaction
        verify(pdfRepository, never()).save(any()); // Ensure save was not called
    }

    @Test
    public void testGenerateAndStorePdf_WhenPdfDoesNotExist_GeneratesAndStoresPdf() throws Exception {
        PdfRequestDTO inputData = createSampleInputData();
        String dataHash = pdfService.hashInputData(inputData); // You can still call it here if you make it package-private

        when(pdfRepository.findByDataHash(dataHash)).thenReturn(Optional.empty());

        // Act
        byte[] pdfContent = pdfService.generateAndStorePdf(inputData);

        // Assert
        assertNotNull(pdfContent, "The generated PDF content should not be null.");
        assertTrue(pdfContent.length > 0, "The generated PDF content should not be empty.");

        ArgumentCaptor<PdfDocument> pdfDocumentCaptor = ArgumentCaptor.forClass(PdfDocument.class);
        verify(pdfRepository, times(1)).save(pdfDocumentCaptor.capture());
        assertEquals(dataHash, pdfDocumentCaptor.getValue().getDataHash());
    }


    private PdfRequestDTO createSampleInputData() {
        // Create and return sample data for testing
        PdfRequestDTO inputData = new PdfRequestDTO();
        inputData.setSeller("XYZ Pvt. Ltd.");
        inputData.setSellerGstin("29AABBCCDD121ZD");
        inputData.setSellerAddress("New Delhi, India");
        inputData.setBuyer("Vedant Computers");
        inputData.setBuyerGstin("29AABBCCDD131ZD");
        inputData.setBuyerAddress("New Delhi, India");

        PdfRequestDTO.ItemDTO item = new PdfRequestDTO.ItemDTO();
        item.setName("Product 1");
        item.setQuantity("12 Nos");
        item.setRate(123.00);
        item.setAmount(1476.00);

        inputData.setItems(List.of(item)); // Set the items in the input data
        return inputData;
    }

    private String hashInputData(PdfRequestDTO inputData) throws Exception {
        // Implement the same hashing logic as in PdfService for testing purposes
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(inputData.toString().getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

