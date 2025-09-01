package org.example.stage_back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("TAXATEUR")
@Data
@EqualsAndHashCode(callSuper = true)
public class Taxateur extends User {
    @ManyToOne
    private Port port;
    private String nom;
    private String prenom;
    private String telephone;
}
