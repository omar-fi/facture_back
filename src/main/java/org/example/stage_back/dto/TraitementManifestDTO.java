package org.example.stage_back.dto;


import lombok.Data;
import java.util.List;

@Data
public class TraitementManifestDTO {
    private Long manifestId;
    private Long agentId;
    private List<LigneDTO> lignes;
}

