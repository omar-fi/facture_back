package org.example.stage_back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TAXATEUR")
public class Taxateur extends User {
    @ManyToOne
    private Port port;
}
