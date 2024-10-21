package com.example.pdf_generator.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entity class representing a PDF document stored in the database.
 */
@Entity
@Table(name = "pdf_documents")
public class PdfDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String dataHash;

    @Lob
    private byte[] pdfContent;

//    Default constructor for JPA.
    public PdfDocument() {
    }

    /**
     * Constructor to create a PdfDocument with specified data hash and PDF content.
     *
     * @param dataHash   a unique hash representing the data of the PDF
     * @param pdfContent the content of the PDF as a byte array
     */
    public PdfDocument(String dataHash, byte[] pdfContent) {
        this.dataHash = dataHash;
        this.pdfContent = pdfContent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public byte[] getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(byte[] pdfContent) {
        this.pdfContent = pdfContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdfDocument that = (PdfDocument) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dataHash, that.dataHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataHash);
    }
}