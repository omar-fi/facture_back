package org.example.stage_back.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
@Data
public class Admin extends User {


}

