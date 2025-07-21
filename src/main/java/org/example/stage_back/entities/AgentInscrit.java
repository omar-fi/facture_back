package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentInscrit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;

    private String raisonSociale;
    private String portDemande;
    private Double ICE;
    @Enumerated(EnumType.STRING)
    private Statut statut;
    private Date dateInscription;
    private String password;
    public enum Statut {
        EN_ATTENTE, VALIDE, REJETE
    }
}
