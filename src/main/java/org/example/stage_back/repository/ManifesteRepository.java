package org.example.stage_back.repository;

import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.dto.ManifestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
            "m.createdBy, " +
            "m.escale.id)"+
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
            "m.createdBy, " +
            "m.escale.id)"+

            "FROM Manifeste m " +
            "LEFT JOIN m.navire n " +
            "LEFT JOIN m.port p " +
            "WHERE m.statut = :statut AND m.processedBy = :taxateurId " +
            "ORDER BY m.dateDepotManifest DESC")
    List<Manifeste> findByStatutAndTaxateurId(Manifeste.StatutManifest statut, Long taxateurId);

    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
            "m.id, n.nom, p.nom, m.dateDepotManifest, m.trafic, m.createdBy, m.escale.id) " +
            "FROM Manifeste m " +
            "LEFT JOIN m.navire n " +
            "LEFT JOIN m.port p " +
            "WHERE m.statut = :statut AND m.processedBy = :taxateurId " +
            "ORDER BY m.dateDepotManifest DESC")
    List<ManifestDTO> findTraitesByTaxateurId(Manifeste.StatutManifest statut, Long taxateurId);


    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
                "m.id, n.nom, p.nom, m.dateDepotManifest, m.trafic, m.createdBy, m.escale.id) " +
                "FROM Manifeste m " +
                "LEFT JOIN m.navire n " +
                "LEFT JOIN m.port p " +
                "WHERE m.statut = :statut " +
                "ORDER BY m.dateDepotManifest DESC")
        List<ManifestDTO> findManifestsByStatut(Manifeste.StatutManifest statut);



    // Pour les manifests en attente
    @Query("SELECT new org.example.stage_back.dto.ManifestDTO(" +
            "m.id, " +
            "n.nom, " +
            "p.nom, " +
            "m.dateDepotManifest, " +
            "m.trafic, " +
            "m.createdBy, " +
            "m.escale.id)"+
            " FROM Manifeste m " +
            " LEFT JOIN m.navire n " +
            " LEFT JOIN m.port p " +
            " WHERE m.statut = :statut " +
            " ORDER BY m.dateDepotManifest DESC")
    List<ManifestDTO> findEnAttenteByStatut(Manifeste.StatutManifest statut);
    @Query("SELECT m FROM Manifeste m LEFT JOIN FETCH m.manifestLines WHERE m.id = :id")
    Optional<Manifeste> findByIdWithLines(@Param("id") Long id);


    List<Manifeste> findByUser_Id(Long id);

    Optional<Manifeste> findByEscale_Id(Long escaleId);
}
