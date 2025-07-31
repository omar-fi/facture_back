package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifSpecifique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer portId;
    private String categorie;
    private String libelle;
    @Enumerated(EnumType.STRING) // pour stocker "m3", "Tonne", "Unité" comme texte
    private Unite unite;
    private String groupName;
    private Double tarifUnitaire;
} 