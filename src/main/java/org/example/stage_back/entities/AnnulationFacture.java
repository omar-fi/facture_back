package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnulationFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private FactureEntete factureEntete;

    private Date dateAnnulation;
    private String motifAnnulation;

    @ManyToOne
    private User utilisateur;
} 