package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureEntete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Escale escale;

    @OneToMany(mappedBy = "factureEntete")
    private List<FactureDetail> details;

    @OneToMany(mappedBy = "factureEntete")
    private List<EncaissementFacture> encaissements;

    @OneToMany(mappedBy = "factureEntete")
    private List<AnnulationFacture> annulations;

    private Date dateEmissionFact;
    private Date dateReglementFact;
    private Date dateAnnulationFact;
} 