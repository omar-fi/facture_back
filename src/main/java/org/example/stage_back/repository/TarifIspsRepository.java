package org.example.stage_back.repository;

import org.example.stage_back.entities.TarifIsps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TarifIspsRepository extends JpaRepository<TarifIsps, Integer> {
    
    @Query("SELECT t FROM TarifIsps t WHERE LOWER(t.libelle) = LOWER(:libelle)")
    Optional<TarifIsps> findByLibelleIgnoreCase(@Param("libelle") String libelle);
} 