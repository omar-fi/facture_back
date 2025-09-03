package org.example.stage_back.dto;
import lombok.Data;

@Data
public class LigneDTO {
    private Long id;
    private String description;
    private Double montant;
}