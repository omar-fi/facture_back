package org.example.stage_back.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int categorie;
    private String libelle;
    @Enumerated(EnumType.STRING)
    private Unite unite;
    private String groupName;
}
