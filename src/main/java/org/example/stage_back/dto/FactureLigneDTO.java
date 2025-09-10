package org.example.stage_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureLigneDTO {
    private String libelleMH;   // de manifest_line
    private String unite;       // de categories
    private Double quantite;    // poids de manifest_line
    private Double montantHT;   // tarif * quantite * occurrences
    private Long repetitions;// nombre de fois que la marchandise se répète
    private double tarifUnitaire; // ✅ nouveau champ

}
