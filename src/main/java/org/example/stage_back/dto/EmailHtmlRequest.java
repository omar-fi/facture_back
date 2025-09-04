package org.example.stage_back.dto;


import lombok.Data;

@Data
public class EmailHtmlRequest {
    private String email;
    private String subject;
    private String message;
    private String htmlContent; // Nouveau champ pour le corps HTML
}