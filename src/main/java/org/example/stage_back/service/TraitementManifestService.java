package org.example.stage_back.service;

import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.dto.TraitementManifestRequest;
import org.example.stage_back.entities.*;
import org.example.stage_back.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TraitementManifestService {

    private static final Logger logger = LoggerFactory.getLogger(TraitementManifestService.class);

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    @Autowired
    private FactureDetailRepository factureDetailRepository;

    @Autowired
    private ManifestLineRepository manifestLineRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Récupère tous les manifests (pour debug)
     */
    public List<ManifestDTO> getAllManifests() {
        logger.info("Récupération de tous les manifests...");
        List<Manifeste> manifests = manifesteRepository.findAll();
        logger.info("Nombre total de manifests trouvés: {}", manifests.size());
        
        List<ManifestDTO> manifestDTOs = manifests.stream()
            .map(ManifestDTO::fromEntity)
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
        
        logger.info("Nombre de DTOs générés: {}", manifestDTOs.size());
        return manifestDTOs;
    }

    /**
     * Récupère les manifests en attente de traitement
     */
    public List<ManifestDTO> getManifestsEnAttente() {
        logger.info("Récupération des manifests en attente...");
        
        // Récupérer tous les manifests pour debug
        List<Manifeste> allManifests = manifesteRepository.findAll();
        logger.info("Total manifests en base: {}", allManifests.size());
        
        // Lister les détails de chaque manifest
        for (Manifeste manifest : allManifests) {
            logger.info("Manifest ID: {}, Statut: {}, CreatedBy: {}, ProcessedBy: {}, Port: {}, User: {}", 
                manifest.getId(),
                manifest.getStatut(),
                manifest.getCreatedBy(),
                manifest.getProcessedBy(),
                manifest.getPort() != null ? manifest.getPort().getId() : "null",
                manifest.getUser() != null ? manifest.getUser().getId() : "null"
            );
        }
        
        // Filtrer par statut EN_ATTENTE
        List<Manifeste> manifestsEnAttente = manifesteRepository.findByStatut(Manifeste.StatutManifest.EN_ATTENTE);
        logger.info("Manifests en attente trouvés: {}", manifestsEnAttente.size());
        
        return manifestsEnAttente.stream()
            .map(ManifestDTO::fromEntity)
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    /**
     * Récupère les manifests traités par un taxateur
     */
    public List<ManifestDTO> getManifestsTraites(Integer taxateurId) {
        logger.info("Récupération des manifests traités par le taxateur: {}", taxateurId);

        // Convertir Integer en Long
        Long taxateurIdLong = taxateurId != null ? taxateurId.longValue() : null;

        List<Manifeste> manifests = manifesteRepository.findByProcessedByAndStatut(taxateurIdLong, Manifeste.StatutManifest.TRAITE);

        return manifests.stream()
                .map(ManifestDTO::fromEntity)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }


    /**
     * Récupère un manifest par son ID
     */
    public ManifestDTO getManifestById(Integer id) {
        logger.info("Récupération du manifest avec l'ID: {}", id);
        Optional<Manifeste> manifest = manifesteRepository.findById(Long.valueOf(id));
        
        return manifest.map(ManifestDTO::fromEntity).orElse(null);
    }

    /**
     * Traite un manifest et génère une facture
     */
    public FactureEntete traiterManifest(TraitementManifestRequest request, Integer taxateurId) {
        logger.info("Traitement du manifest: {} par le taxateur: {}", request.getManifestId(), taxateurId);
        
        // Récupérer le manifest
        Manifeste manifest = manifesteRepository.findById(Long.valueOf(request.getManifestId()))
            .orElseThrow(() -> new RuntimeException("Manifest non trouvé"));
        
        // Mettre à jour le statut
        manifest.setStatut(Manifeste.StatutManifest.TRAITE);
        manifest.setProcessedBy(Long.valueOf(taxateurId));
        manifest.setDateTraitement(new Date());
        manifest.setCommentairesTraitement(request.getCommentaires());
        
        // Sauvegarder le manifest
        manifesteRepository.save(manifest);
        
        // Générer la facture
        FactureEntete facture = genererFacture(manifest, request, taxateurId);
        
        // Calculer le montant total à partir des détails de facture
        List<FactureDetail> details = factureDetailRepository.findByFactureEntete(facture);
        double montantTotal = details.stream()
            .mapToDouble(detail -> detail.getMontantTTC() != null ? detail.getMontantTTC().doubleValue() : 0.0)
            .sum();
        
        manifest.setMontantTotal(montantTotal);
        manifesteRepository.save(manifest);
        
        // Notifier l'agent que sa facture est prête
        try {
            emailService.notifierFactureGeneree(facture);
            logger.info("Notification envoyée à l'agent pour la facture: {}", facture.getId());
        } catch (Exception e) {
            logger.warn("Impossible d'envoyer la notification à l'agent", e);
        }
        
        return facture;
    }

    /**
     * Génère une facture à partir d'un manifest
     */
    private FactureEntete genererFacture(Manifeste manifest, TraitementManifestRequest request, Integer taxateurId) {
        logger.info("Génération de la facture pour le manifest: {}", manifest.getId());
        
        FactureEntete facture = new FactureEntete();
        facture.setEscale(manifest.getEscale());
        facture.setManifeste(manifest);
        facture.setDateEmissionFact(new Date());
        
        // Sauvegarder la facture
        facture = factureEnteteRepository.save(facture);
        
        // Créer les détails de facture
        for (TraitementManifestRequest.LigneTraitement ligne : request.getLignes()) {
            FactureDetail detail = new FactureDetail();
            detail.setFactureEntete(facture);
            detail.setCategorieId(ligne.getManifestLineId());
            detail.setNumeroLigneFact(ligne.getManifestLineId());
            
            // Calculer les montants (exemple simple)
            double montantHT = ligne.getTarifUnitaire() * 1.0; // Quantité fixée à 1 pour l'exemple
            double montantTVA = montantHT * 0.20; // TVA 20%
            double montantTTC = montantHT + montantTVA;
            
            detail.setMontantHT(java.math.BigDecimal.valueOf(montantHT));
            detail.setMontantTVA(java.math.BigDecimal.valueOf(montantTVA));
            detail.setMontantTTC(java.math.BigDecimal.valueOf(montantTTC));
            
            factureDetailRepository.save(detail);
        }
        
        return facture;
    }
}
