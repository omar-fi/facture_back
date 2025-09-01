package org.example.stage_back.dto;

import lombok.Data;
import java.util.List;

@Data
public class TraitementManifestRequest {
    private Integer manifestId;
    private String commentaires;
    private List<LigneTraitement> lignes;
    
    @Data
    public static class LigneTraitement {
        private Integer manifestLineId;
        private Double tarifUnitaire;
    }
}
