package org.example.stage_back.controller;

import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.service.FacturePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    @Autowired
    private FacturePdfService facturePdfService;

    /**
     * Liste toutes les factures
     */
    @GetMapping("/")
    public ResponseEntity<List<FactureEntete>> listFactures() {
        try {
            List<FactureEntete> factures = factureEnteteRepository.findAll();
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Récupère une facture par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactureEntete> getFacture(@PathVariable Integer id) {
        try {
            return factureEnteteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Télécharge une facture en PDF
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadFacturePdf(@PathVariable Integer id) {
        try {
            FactureEntete facture = factureEnteteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

            byte[] pdfBytes = facturePdfService.genererFacturePdf(facture);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=facture_" + id + ".pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Liste les factures d'un agent spécifique
     */
    @GetMapping("/agent/{agentId}")
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
}
