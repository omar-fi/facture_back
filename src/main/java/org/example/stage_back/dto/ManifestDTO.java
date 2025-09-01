package org.example.stage_back.dto;

import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.entities.Navire;
import org.example.stage_back.entities.Port;

import java.util.Date;

public record ManifestDTO(
        Integer id,
        String navire,
        String port,
        Date dateDepot,
        String trafic,
        Long createdBy
) {
    // Record avec constructeur automatique
    
    // Factory method depuis l'entité Manifeste
    public static ManifestDTO fromEntity(Manifeste manifeste) {
        if (manifeste == null) return null;
        return new ManifestDTO(
                manifeste.getId(),
                manifeste.getNavire() != null ? manifeste.getNavire().getNom() : null,
                manifeste.getPort() != null ? manifeste.getPort().getNom() : null,
                manifeste.getDateDepotManifest(),
                manifeste.getTrafic(),
                manifeste.getCreatedBy()
        );
    }
}