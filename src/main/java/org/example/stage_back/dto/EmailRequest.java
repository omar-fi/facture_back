package org.example.stage_back.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String email;
    private String subject;
    private String message;
    private String pdfBase64;
    private String filename;
}
