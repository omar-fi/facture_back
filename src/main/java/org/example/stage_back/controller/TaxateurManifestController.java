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
            List<Manifeste> manifests = manifesteRepository.findByStatut(Manifeste.StatutManifest.EN_ATTENTE);
            // Conversion Manifeste -> ManifestDTO
            List<ManifestDTO> dtoList = manifests.stream()
                    .map(ManifestDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    // 2. Manifests traités par un taxateur
    @GetMapping("/traites")
    public ResponseEntity<List<ManifestDTO>> getTraites(@RequestParam Long taxateurId) {
        try {
            List<Manifeste> manifests = manifesteRepository.findByStatutAndTaxateurId(Manifeste.StatutManifest.TRAITE, taxateurId);
            List<ManifestDTO> dtoList = manifests.stream()
                    .map(ManifestDTO::fromEntity)
                    .toList();
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


}
