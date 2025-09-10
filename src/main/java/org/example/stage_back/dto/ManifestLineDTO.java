package org.example.stage_back.dto;

import lombok.Data;
import org.example.stage_back.entities.ManifestLine;

@Data
public class ManifestLineDTO {
    private Integer id;
    private String libelleMH;
    private Integer codeSH;
    private Double poids;
    private String marchandise;
    
    public static ManifestLineDTO fromEntity(ManifestLine line) {
        if (line == null) return null;
        
        ManifestLineDTO dto = new ManifestLineDTO();
        dto.setId(line.getId());
        dto.setLibelleMH(line.getLibellemh());
        dto.setCodeSH(line.getCodesh());
        dto.setPoids(line.getPoids());
        dto.setMarchandise(line.getMarchandise());
        return dto;
    }
}
