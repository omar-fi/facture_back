package org.example.stage_back.repository;

import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.dto.ManifestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManifesteRepository extends JpaRepository<Manifeste, Long> {

    // Manifeste en attente (non traités)
    List<Manifeste> findByProcessedByIsNull();

    // Manifeste traités par un taxateur (processedBy = id du taxateur)
    List<Manifeste> findByProcessedByAndStatut(Long processedBy, Manifeste.StatutManifest statut);


    // Par port
    List<Manifeste> findByPortId(Long portId);

    // Par statut
    List<Manifeste> findByStatut(Manifeste.StatutManifest statut);

    // Créé par
    List<Manifeste> findByCreatedBy(Integer createdBy);

    // Retourne un DTO pour la liste des manifests
    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
            "m.id, " +
            "n.nom, " +
            "p.nom, " +
            "m.dateDepotManifest, " +
            "m.trafic, " +
            "m.createdBy) " +
            "FROM Manifeste m " +
            "LEFT JOIN m.navire n " +
            "LEFT JOIN m.port p " +
            "ORDER BY m.dateDepotManifest DESC")
    List<ManifestDTO> findAllListItems();

    // Correction : récupération des manifests traités par un taxateur précis
    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
            "m.id, " +
            "n.nom, " +
            "p.nom, " +
            "m.dateDepotManifest, " +
            "m.trafic, " +
            "m.createdBy) " +
            "FROM Manifeste m " +
            "LEFT JOIN m.navire n " +
            "LEFT JOIN m.port p " +
            "WHERE m.statut = :statut AND m.processedBy = :taxateurId " +
            "ORDER BY m.dateDepotManifest DESC")
    List<ManifestDTO> findByStatutAndTaxateurId(Manifeste.StatutManifest statut, Long taxateurId);

    // Nouvelle méthode : récupération des manifests en attente directement en DTO
    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
            "m.id, " +
            "n.nom, " +
            "p.nom, " +
            "m.dateDepotManifest, " +
            "m.trafic, " +
            "m.createdBy) " +
            "FROM Manifeste m " +
            "LEFT JOIN m.navire n " +
            "LEFT JOIN m.port p " +
            "WHERE m.statut = :statut " +
            "ORDER BY m.dateDepotManifest DESC")
    List<ManifestDTO> findManifestsEnAttenteByStatut(Manifeste.StatutManifest statut);
}
