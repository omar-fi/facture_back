package org.example.stage_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.dto.FactureDetailDTO;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.service.FactureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;
    private final AgentRepository agentRepository;
    private final ManifesteRepository manifesteRepository;
    private final FactureEnteteRepository factureEnteteRepository;

    @GetMapping("/calcule")
    public ResponseEntity<FactureDetailDTO> calculeFacture(
            @RequestParam Long agentId,
            @RequestParam Long manifesteId) {

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("❌ Agent introuvable"));

        Manifeste manifeste = manifesteRepository.findByIdWithLines(manifesteId)
                .orElseThrow(() -> new RuntimeException("❌ Manifeste introuvable"));


        FactureDetailDTO dto = factureService.calculerFacture(agent, manifeste);

        return ResponseEntity.ok(dto);
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

    // DTO pour recevoir l'agent et le manifeste depuis le body
    public static class FactureRequest {
        private Agent agent;
        private Manifeste manifeste;

        public Agent getAgent() {
            return agent;
        }

        public void setAgent(Agent agent) {
            this.agent = agent;
        }

        public Manifeste getManifeste() {
            return manifeste;
        }

        public void setManifeste(Manifeste manifeste) {
            this.manifeste = manifeste;
        }
    }
}
