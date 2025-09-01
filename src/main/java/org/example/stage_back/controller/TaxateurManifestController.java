package org.example.stage_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.dto.ManifestDTO;
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

    // 1. Manifests en attente - Récupération depuis la base de données
    @GetMapping("/en-attente")
    public ResponseEntity<List<ManifestDTO>> getEnAttente() {
        try {
            List<ManifestDTO> manifests = manifesteRepository.findManifestsEnAttenteByStatut(Manifeste.StatutManifest.EN_ATTENTE);
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 2. Manifests traités par un taxateur
    @GetMapping("/traites")
    public ResponseEntity<List<ManifestDTO>> getTraites(@RequestParam Long taxateurId) {
        try {
            List<ManifestDTO> manifests = manifesteRepository.findByStatutAndTaxateurId(Manifeste.StatutManifest.TRAITE, taxateurId);
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
