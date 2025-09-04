package org.example.stage_back.dto;

public class FactureMailRequestDTO {
    private String emailDestinataire; // <-- Champ original
    private String subject;
    private String message;
    private String pdfBase64;
    private String filename;
    private Long factureId;

    // Getters et setters
    public String getEmailDestinataire() { return emailDestinataire; }
    public void setEmailDestinataire(String emailDestinataire) { this.emailDestinataire = emailDestinataire; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPdfBase64() { return pdfBase64; }
    public void setPdfBase64(String pdfBase64) { this.pdfBase64 = pdfBase64; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public Long getFactureId() { return factureId; }
    public void setFactureId(Long factureId) { this.factureId = factureId; }
}
