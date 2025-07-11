package org.example.stage_back.dto;

import lombok.Data;

@Data
public class AgentInscriptionRequest {
    private String fullName;
    private String email;
    private String raisonSociale;
    private String portDemande;
    private Double ICE;
    private String password;
} 