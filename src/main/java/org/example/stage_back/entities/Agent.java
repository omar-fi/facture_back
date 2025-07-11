package org.example.stage_back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@DiscriminatorValue("AGENT")
@Data
public class Agent extends User {
    @ManyToOne
    private Port port;
    String raisonSociale;
    String portDemande ;
    Long  ICE;
}
