package org.example.stage_back.dto;

import lombok.Data;
import org.example.stage_back.entities.FactureEntete;

import java.util.Date;

@Data
public class FactureDTO {
    private Integer id;
    private Date dateEmissionFact;
    private Date dateReglementFact;
    private Integer manifesteId;
    private Integer escaleId;
    private String portNom;
    private String agentEmail;
    
    public static FactureDTO fromEntity(FactureEntete facture) {
        if (facture == null) return null;
        
        FactureDTO dto = new FactureDTO();
        dto.setId(facture.getId());
        dto.setDateEmissionFact(facture.getDateEmissionFact());
        dto.setDateReglementFact(facture.getDateReglementFact());
        
        if (facture.getManifeste() != null) {
            dto.setManifesteId(facture.getManifeste().getId());
            if (facture.getManifeste().getUser() != null) {
                dto.setAgentEmail(facture.getManifeste().getUser().getEmail());
            }
        }
        
        if (facture.getEscale() != null) {
            dto.setEscaleId(facture.getEscale().getId());
            if (facture.getEscale().getPort() != null) {
                dto.setPortNom(facture.getEscale().getPort().getNom());
            }
        }
        
        return dto;
    }
}
