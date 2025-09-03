package org.example.stage_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.dto.TraitementManifestDTO;
import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.repository.ManifesteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taxateur/manifests")
@RequiredArgsConstructor
public class TaxateurManifestController {

    private final ManifesteRepository manifesteRepository;

    @GetMapping("/en-attente")
    public ResponseEntity<List<ManifestDTO>> getEnAttente() {
        try {
            List<ManifestDTO> dtoList = manifesteRepository.findManifestsByStatut(Manifeste.StatutManifest.EN_ATTENTE);
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace(); // ← pour debug
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/traites")
    public ResponseEntity<List<ManifestDTO>> getTraites(@RequestParam Long taxateurId) {
        try {
            List<ManifestDTO> dtoList = manifesteRepository.findTraitesByTaxateurId(Manifeste.StatutManifest.TRAITE, taxateurId);
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace(); // ← Important pour voir la vraie erreur
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/traiter")
    public ResponseEntity<?> traiterManifest(
            @RequestParam Long taxateurId,
            @RequestBody TraitementManifestDTO payload
    ) {
        try {
            Manifeste manifeste = manifesteRepository.findById(payload.getManifestId())
                    .orElseThrow(() -> new RuntimeException("Manifeste introuvable"));

            // Mise à jour du statut et du taxateur
            manifeste.setStatut(Manifeste.StatutManifest.TRAITE);
            manifeste.setProcessedBy(taxateurId);

            // Ici tu peux traiter les lignes si besoin, sinon ignorer
            // payload.getLignes() ...

            manifesteRepository.save(manifeste);

            return ResponseEntity.ok("Manifeste traité avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors du traitement : " + e.getMessage());
        }
    }



}




