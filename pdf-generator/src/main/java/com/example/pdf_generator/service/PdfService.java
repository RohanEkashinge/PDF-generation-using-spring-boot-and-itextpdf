package com.example.pdf_generator.service;


import com.example.pdf_generator.dto.PdfRequestDTO;
import com.example.pdf_generator.model.PdfDocument;
import com.example.pdf_generator.repository.PdfRepository;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Optional;

/**
 * Service class for generating and storing PDF documents.
 */
@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    @Autowired
    private PdfRepository pdfRepository;

    /**
     * Generates a PDF document based on the provided input data and stores it in the database.
     * If a PDF with the same data hash already exists, it returns the existing PDF.
     *
     * @param inputData the data required to generate the PDF
     * @return the generated PDF content as a byte array
     * @throws Exception if an error occurs during PDF generation or storage
     */
    public byte[] generateAndStorePdf(PdfRequestDTO inputData) throws Exception {
        String dataHash = hashInputData(inputData);

        // Check if the PDF with the same data already exists
        Optional<PdfDocument> existingPdf = pdfRepository.findByDataHash(dataHash);
        if (existingPdf.isPresent()) {
            logger.info("Returning existing PDF from database.");
            return existingPdf.get().getPdfContent();
        }

        // Generate new PDF
        byte[] pdfContent = generatePdf(inputData);

        // Store the generated PDF
        PdfDocument pdfDocument = new PdfDocument(dataHash, pdfContent);
        pdfRepository.save(pdfDocument);

        logger.info("New PDF generated and stored in database.");
        return pdfContent;
    }

    /**
     * Generates a SHA-256 hash from the input data.
     *
     * @param inputData the data to hash
     * @return the hashed string representation of the input data
     * @throws Exception if an error occurs during hashing
     */
    public String hashInputData(PdfRequestDTO inputData) throws Exception {
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

    /**
     * Generates a PDF document from the provided input data.
     *
     * @param inputData the data required to generate the PDF
     * @return the generated PDF content as a byte array
     */
    private byte[] generatePdf(PdfRequestDTO inputData) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Create a single table with 4 columns for uniformity
        float[] columnWidths = {4, 4, 2, 2}; // Set custom column widths for each part
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100)); // Center table, 100% page width

        // 1st Row: Seller and Buyer Information
        Cell sellerCell = new Cell(1, 2); // 1 row, 2 columns for Seller
        sellerCell.add(new Paragraph("Seller:").setBold());
        sellerCell.add(new Paragraph(inputData.getSeller() + "\n" +
                inputData.getSellerAddress() + "\n" +
                "GSTIN: " + inputData.getSellerGstin()));
        table.addCell(sellerCell);

        Cell buyerCell = new Cell(1, 2); // 1 row, 2 columns for Buyer
        buyerCell.add(new Paragraph("Buyer:").setBold());
        buyerCell.add(new Paragraph(inputData.getBuyer() + "\n" +
                inputData.getBuyerAddress() + "\n" +
                "GSTIN: " + inputData.getBuyerGstin()));
        table.addCell(buyerCell);

        // 2nd Row: Header for Items, Quantity, Rate, Amount
        table.addCell(new Paragraph("Item").setBold());
        table.addCell(new Paragraph("Quantity").setBold());
        table.addCell(new Paragraph("Rate").setBold());
        table.addCell(new Paragraph("Amount").setBold());

        // 3rd Row and beyond: Add item rows dynamically from inputData
        for (PdfRequestDTO.ItemDTO item : inputData.getItems()) {
            table.addCell(new Paragraph(item.getName()));
            table.addCell(new Paragraph(item.getQuantity()));
            table.addCell(new Paragraph(item.getRate().toString()));
            table.addCell(new Paragraph(item.getAmount().toString()));
        }

        // Final Row: Blank row with double height
        Cell blankCell = new Cell(1, 4); // Span across all 4 columns
        blankCell.setHeight(40f); // Set double height for the blank cell
        table.addCell(blankCell);

        // Add table to the document
        document.add(table);

        // Close the document
        document.close();
        logger.info("PDF generation completed successfully.");

        return outputStream.toByteArray();
    }

    public void setPdfRepository(PdfRepository pdfRepository) {
        this.pdfRepository = pdfRepository;
    }


}