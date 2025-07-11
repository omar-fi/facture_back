package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Navire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String numLloyd;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TypeNavire typeNavire;
} 