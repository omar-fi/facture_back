package org.example.stage_back.dto;

import lombok.Data;

@Data
public class AgentInscriptionRequest {
    private String fullName;
    private String email;
    private String raisonSociale;
    private Long portId;
    private Double ice;
    private String password;
} 