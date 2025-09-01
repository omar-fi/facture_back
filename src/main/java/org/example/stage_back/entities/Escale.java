package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Escale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Navire navire;

    @ManyToOne
    private Port port;

    @OneToMany(mappedBy = "escale")
    private List<Manifeste> manifestes;

    @OneToMany(mappedBy = "escale")
    private List<FactureEntete> factures;

    private Date dateArrivee;
    private Date dateDepart;
} 