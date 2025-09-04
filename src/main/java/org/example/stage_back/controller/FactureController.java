package org.example.stage_back.controller;

import org.example.stage_back.dto.EmailRequest;
import org.example.stage_back.dto.FactureMailRequestDTO;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    private final FactureEnteteRepository factureEnteteRepository;
    private final FactureService factureService;

    @Autowired
    public FactureController(FactureEnteteRepository factureEnteteRepository,
                             FactureService factureService) {
        this.factureEnteteRepository = factureEnteteRepository;
        this.factureService = factureService;
    }

    @PostMapping("/{id}/envoyer")
    public ResponseEntity<String> envoyerFacture(@PathVariable Long id,
                                                 @RequestParam String email) {
        try {
            FactureMailRequestDTO request = new FactureMailRequestDTO();
            request.setFactureId(id);
            request.setEmailDestinataire(email);

            factureService.envoyerFactureParMail(request);

            return ResponseEntity.ok("Facture envoyée avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi de la facture: " + e.getMessage());
        }
    }

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        try {
            // Appelez un nouveau service ou modifiez l'existant pour qu'il prenne l'objet EmailRequest
            factureService.envoyerEmailAvecPdf(request); // Créez cette nouvelle méthode
            return ResponseEntity.ok("Mail envoyé !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactureEntete> getFactureById(@PathVariable Long id) {
        return factureEnteteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
