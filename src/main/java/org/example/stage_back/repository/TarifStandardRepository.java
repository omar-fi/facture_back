package org.example.stage_back.repository;

import org.example.stage_back.entities.TarifStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifStandardRepository extends JpaRepository<TarifStandard, Long> {

    // ✅ Recherche par catégorie uniquement (sans port)
    List<TarifStandard> findByCategorie(String categorie);

}
