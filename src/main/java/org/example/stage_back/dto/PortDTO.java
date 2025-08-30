package org.example.stage_back.dto;

import lombok.Data;
import org.example.stage_back.entities.Port;

@Data
public class PortDTO {
    private Long id;
    private String nom;
    private String ville;
    
    public static PortDTO fromEntity(Port port) {
        if (port == null) return null;
        
        PortDTO dto = new PortDTO();
        dto.setId(port.getId());
        dto.setNom(port.getNom());
        dto.setVille(port.getVille());
        return dto;
    }
}
