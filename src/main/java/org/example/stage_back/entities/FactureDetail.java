package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private FactureEntete factureEntete;

    private Integer categorieId;
    private Integer numeroLigneFact;
    private BigDecimal montantHT;
    private BigDecimal montantTVA;
    private BigDecimal montantTR;
    private BigDecimal montantTTC;
} 