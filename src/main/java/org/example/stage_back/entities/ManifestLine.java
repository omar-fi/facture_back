package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManifestLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Manifeste manifeste;

    private Integer codeSH;
    private String categorie;
    private String marchandise;
    private String libelleMH;
    private Double poids;
    private Double volume;
} 