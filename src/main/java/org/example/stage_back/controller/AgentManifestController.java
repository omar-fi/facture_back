package org.example.stage_back.controller;

import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agent")
public class AgentManifestController {

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    /**
     * Récupère tous les manifests d'un agent
     */
    @GetMapping("/manifests/{agentId}")
    public ResponseEntity<List<ManifestDTO>> getManifestsByAgent(@PathVariable Integer agentId) {
        try {
            List<ManifestDTO> manifests = manifesteRepository.findByCreatedBy(agentId)
                .stream()
                .map(ManifestDTO::fromEntity)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère toutes les factures d'un agent
     */
    @GetMapping("/factures/{agentId}")
    public ResponseEntity<List<FactureEntete>> getFacturesByAgent(@PathVariable Integer agentId) {
        try {
            // Récupérer les factures liées aux manifests de l'agent
            List<FactureEntete> factures = factureEnteteRepository.findAll().stream()
                .filter(facture -> facture.getManifeste() != null && 
                                 facture.getManifeste().getCreatedBy() != null &&
                                 facture.getManifeste().getCreatedBy().equals(agentId))
                .toList();
            
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère le statut d'un manifest spécifique
     */
    @GetMapping("/manifests/{manifestId}/status")
    public ResponseEntity<String> getManifestStatus(@PathVariable Integer manifestId) {
        try {
            return manifesteRepository.findById(manifestId)
                .map(manifest -> ResponseEntity.ok(manifest.getStatut().name()))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
