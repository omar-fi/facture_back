package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.*;

@Entity
@Table(name = "manifeste")
@Data @NoArgsConstructor @AllArgsConstructor
public class Manifeste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "code_client")
    private String codeClient;


    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "navire_id")
    private Navire navire;

    // Port.java
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "port_id")
    private Port port;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "escale_id")
    private Escale escale;
    @Column(name = "date_traitement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTraitement;

    @Column(name = "commentaires_traitement")
    private String commentairesTraitement;

    @Column(name = "montant_total")
    private Double montantTotal;


    @OneToMany(mappedBy = "manifeste", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<ManifestLine> manifestLines = new ArrayList<>();
    // Nouveaux champs pour le workflow
    @Enumerated(EnumType.STRING)
    private StatutManifest statut = StatutManifest.EN_ATTENTE;

    // 👉 RENSEIGNÉE PAR LE SERVICE AU DÉPÔT
    @Column(name = "date_depot_manifest")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDepotManifest;

    private String fichePath;
    private Long createdBy;
    private Long processedBy;


    private String trafic;


    public record ManifestListItemDTO(
            Integer id,
            String navire,
            String port,
            Date dateDepot,
            String trafic,
            Long createdBy
    ) {
        public static ManifestListItemDTO fromEntity(Manifeste manifeste) {
            if (manifeste == null) return null;
            return new ManifestListItemDTO(
                    manifeste.getId(),
                    manifeste.getNavire() != null ? manifeste.getNavire().getNom() : null,
                    manifeste.getPort() != null ? manifeste.getPort().getNom() : null,
                    manifeste.getDateDepotManifest(),
                    manifeste.getTrafic(),
                    manifeste.getCreatedBy()
            );
        }

    }

    public Object getDateDepot() {
        return dateDepotManifest;
    }

    public enum StatutManifest {
        EN_ATTENTE,
        VALIDE,
        REJETE,
        TRAITE
    }





    // 👉 RENSEIGNÉE AUTOMATIQUEMENT PAR HIBERNATE/DB
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    // Getter et Setter pour codeClient
    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }
    /** Helpers */
    public void addLine(ManifestLine line) { line.setManifeste(this); this.manifestLines.add(line); }
    public void clearLines() { for (ManifestLine l : manifestLines) l.setManifeste(null); manifestLines.clear(); }
}