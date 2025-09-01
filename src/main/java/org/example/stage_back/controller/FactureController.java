package org.example.stage_back.controller;

import org.example.stage_back.dto.FactureDTO;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.service.FacturePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    @Autowired
    private FacturePdfService facturePdfService;

    @GetMapping
    public List<FactureDTO> getAllFactures() {
        return factureEnteteRepository.findAll().stream()
                .map(FactureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/agent/{agentId}")
    public List<FactureDTO> getFacturesByAgent(@PathVariable Long agentId) {
        // Récupérer les factures liées aux manifests de l'agent
        return factureEnteteRepository.findByManifeste_User_Id(agentId).stream()
                .map(FactureDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactureDTO> getFactureById(@PathVariable Long id) {
        return factureEnteteRepository.findById(id)
                .map(FactureDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Télécharge une facture en PDF
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFacture(@PathVariable Long id) {
        try {
            FactureEntete facture = factureEnteteRepository.findById(id)
                .orElse(null);
            
            if (facture == null) {
                return ResponseEntity.notFound().build();
            }

            // Générer le PDF
            Resource pdfResource = facturePdfService.generateFacturePdf(facture);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"facture_" + id + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public FactureEntete createFacture(@RequestBody FactureEntete facture) {
        return factureEnteteRepository.save(facture);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FactureEntete> updateFacture(@PathVariable Long id, @RequestBody FactureEntete factureDetails) {
        return factureEnteteRepository.findById(id)
                .map(facture -> {
                    facture.setDateEmissionFact(factureDetails.getDateEmissionFact());
                    facture.setDateReglementFact(factureDetails.getDateReglementFact());
                    FactureEntete updatedFacture = factureEnteteRepository.save(facture);
                    return ResponseEntity.ok(updatedFacture);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable Long id) {
        return factureEnteteRepository.findById(id)
                .map(facture -> {
                    factureEnteteRepository.delete(facture);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
