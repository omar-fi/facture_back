package org.example.stage_back.repository;

import org.example.stage_back.entities.Manifeste;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManifesteRepository extends JpaRepository<Manifeste, Integer> {
    List<Manifeste> findByStatut(Manifeste.StatutManifest statut);
    List<Manifeste> findByProcessedByAndStatut(Integer processedBy, Manifeste.StatutManifest statut);
    List<Manifeste> findByPort_Id(Long portId);
    List<Manifeste> findByCreatedBy(Integer createdBy);
} 