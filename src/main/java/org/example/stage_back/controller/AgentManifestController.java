package org.example.stage_back.controller;

import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.dto.FactureDTO;
import org.example.stage_back.dto.ChangePasswordRequest;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.service.FacturePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AgentManifestController {

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FacturePdfService facturePdfService;

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
    public ResponseEntity<List<FactureDTO>> getFacturesByAgent(@PathVariable Integer agentId) {
        try {
            // Récupérer les factures liées aux manifests de l'agent
            List<FactureDTO> factures = factureEnteteRepository.findAll().stream()
                .filter(facture -> facture.getManifeste() != null && 
                                 facture.getManifeste().getCreatedBy() != null &&
                                 facture.getManifeste().getCreatedBy().equals(agentId))
                .map(FactureDTO::fromEntity)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(factures);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Télécharge une facture en PDF
     */
    @GetMapping("/factures/{factureId}/download")
    public ResponseEntity<Resource> downloadFacture(@PathVariable Integer factureId) {
        try {
            FactureEntete facture = factureEnteteRepository.findById(Long.valueOf(factureId))
                    .orElse(null);

            if (facture == null) {
                return ResponseEntity.notFound().build();
            }

            // Générer le PDF
            ByteArrayInputStream pdfStream = facturePdfService.generateFacturePdf(facture);
            ByteArrayResource pdfResource = new ByteArrayResource(pdfStream.readAllBytes());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"facture_" + factureId + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdfResource.contentLength())
                    .body(pdfResource);

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
            return manifesteRepository.findById(Long.valueOf(manifestId))
                .map(manifest -> ResponseEntity.ok(manifest.getStatut().name()))
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Modifie le mot de passe d'un agent
     */
    @PostMapping("/{agentId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long agentId, @RequestBody ChangePasswordRequest request) {
        try {
            // Vérifier que l'agent existe
            Agent agent = agentRepository.findById(agentId)
                .orElse(null);
            
            if (agent == null) {
                return ResponseEntity.badRequest().body("Agent non trouvé");
            }

            // Vérifier que le nouveau mot de passe et sa confirmation correspondent
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Le nouveau mot de passe et sa confirmation ne correspondent pas");
            }

            // Vérifier que le nouveau mot de passe fait au moins 6 caractères
            if (request.getNewPassword().length() < 6) {
                return ResponseEntity.badRequest().body("Le nouveau mot de passe doit contenir au moins 6 caractères");
            }

            // Vérifier l'ancien mot de passe
            if (!passwordEncoder.matches(request.getCurrentPassword(), agent.getPassword())) {
                return ResponseEntity.badRequest().body("L'ancien mot de passe est incorrect");
            }

            // Encoder et sauvegarder le nouveau mot de passe
            agent.setPassword(passwordEncoder.encode(request.getNewPassword()));
            agentRepository.save(agent);

            return ResponseEntity.ok("Mot de passe modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la modification du mot de passe: " + e.getMessage());
        }
    }

    /**
     * Récupère les informations du profil d'un agent
     */
    @GetMapping("/{agentId}/profile")
    public ResponseEntity<?> getAgentProfile(@PathVariable Long agentId) {
        try {
            Agent agent = agentRepository.findById(agentId)
                .orElse(null);
            
            if (agent == null) {
                return ResponseEntity.notFound().build();
            }

            // Retourner les informations du profil (sans le mot de passe)
            return ResponseEntity.ok(Map.of(
                "id", agent.getId(),
                "email", agent.getEmail(),
                "raisonSociale", agent.getRaisonSociale(),
                "port", agent.getPort() != null ? agent.getPort().getNom() : "Non assigné",
                "ice", agent.getICE(),
                "createdAt", agent.getCreatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
