package org.example.stage_back.dto;

import lombok.Data;

@Data
public class TaxateurRequestDTO {
    private String email;
    private String password;
    private Long portId;


}