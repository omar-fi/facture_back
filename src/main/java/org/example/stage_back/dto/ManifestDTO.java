package org.example.stage_back.dto;

import lombok.Data;
import org.example.stage_back.entities.Manifeste;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ManifestDTO {
    private Integer id;
    private String trafic;
    private Date dateDepotManifest;
    private Date createdAt;
    private Integer createdBy;
    private Integer processedBy;
    private String statut;
    private Date dateTraitement;
    private String commentairesTraitement;
    private Double montantTotal;
    
    // Informations du port
    private PortDTO port;
    
    // Informations de l'utilisateur
    private UserDTO user;
    
    // Lignes du manifest (simplifiées)
    private List<ManifestLineDTO> manifestLines;

    public static ManifestDTO fromEntity(Manifeste manifeste) {
        if (manifeste == null) return null;
        
        ManifestDTO dto = new ManifestDTO();
        dto.setId(manifeste.getId());
        dto.setTrafic(manifeste.getTrafic());
        dto.setDateDepotManifest(manifeste.getDateDepotManifest());
        dto.setCreatedAt(manifeste.getCreatedAt());
        dto.setCreatedBy(manifeste.getCreatedBy());
        dto.setProcessedBy(manifeste.getProcessedBy());
        dto.setStatut(manifeste.getStatut() != null ? manifeste.getStatut().name() : null);
        dto.setDateTraitement(manifeste.getDateTraitement());
        dto.setCommentairesTraitement(manifeste.getCommentairesTraitement());
        dto.setMontantTotal(manifeste.getMontantTotal());
        
        // Port
        if (manifeste.getPort() != null) {
            dto.setPort(PortDTO.fromEntity(manifeste.getPort()));
        }
        
        // User
        if (manifeste.getUser() != null) {
            dto.setUser(UserDTO.fromEntity(manifeste.getUser()));
        }
        
        // Manifest lines
        if (manifeste.getManifestLines() != null) {
            dto.setManifestLines(manifeste.getManifestLines().stream()
                .map(ManifestLineDTO::fromEntity)
                .filter(line -> line != null)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
