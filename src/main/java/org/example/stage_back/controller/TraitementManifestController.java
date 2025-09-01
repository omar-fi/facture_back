package org.example.stage_back.controller;

import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.dto.TraitementManifestRequest;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.service.TraitementManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traitement/manifests") // <- Mapping changé pour éviter conflit
public class TraitementManifestController {

    @Autowired
    private TraitementManifestService traitementManifestService;

    /**
     * Liste tous les manifests (pour debug)
     */
    @GetMapping("/all")
    public ResponseEntity<List<ManifestDTO>> getAllManifests() {
        try {
            List<ManifestDTO> manifests = traitementManifestService.getAllManifests();
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Liste les manifests en attente de traitement
     */
    @GetMapping("/en-attente-traitement")
    public ResponseEntity<List<ManifestDTO>> getManifestsEnAttente() {
        try {
            List<ManifestDTO> manifests = traitementManifestService.getManifestsEnAttente();
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Liste les manifests traités par un taxateur
     */
    @GetMapping("/traites")
    public ResponseEntity<List<ManifestDTO>> getManifestsTraites(
            @RequestParam("taxateurId") Integer taxateurId) {
        try {
            List<ManifestDTO> manifests = traitementManifestService.getManifestsTraites(taxateurId);
            return ResponseEntity.ok(manifests);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère un manifest par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ManifestDTO> getManifestById(@PathVariable Integer id) {
        try {
            ManifestDTO manifest = traitementManifestService.getManifestById(id);
            if (manifest != null) {
                return ResponseEntity.ok(manifest);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Traite un manifest (change le statut et génère la facture)
     */
    @PostMapping("/traiter")
    public ResponseEntity<FactureEntete> traiterManifest(
            @RequestParam("taxateurId") Integer taxateurId,
            @RequestBody TraitementManifestRequest request) {
        try {
            FactureEntete facture = traitementManifestService.traiterManifest(request, taxateurId);
            return ResponseEntity.ok(facture);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
