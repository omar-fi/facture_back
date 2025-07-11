package org.example.stage_back.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manifeste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Port port;

    @ManyToOne
    private Escale escale;

    @OneToMany(mappedBy = "manifeste")
    private List<ManifestLine> manifestLines;

    private Date dateDepotManifest;
    private String fichePath;
    private Integer createdBy;
    private Integer processedBy;
    private String trafic;
    private Date createdAt;
}
