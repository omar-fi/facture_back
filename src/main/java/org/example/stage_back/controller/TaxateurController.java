package org.example.stage_back.controller;

import org.example.stage_back.dto.ChangePasswordRequest;
import org.example.stage_back.entities.Taxateur;
import org.example.stage_back.repository.TaxateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/taxateur")
public class TaxateurController {

    @Autowired
    private TaxateurRepository taxateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Modifie le mot de passe d'un taxateur
     */
    @PostMapping("/{taxateurId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long taxateurId, @RequestBody ChangePasswordRequest request) {
        try {
            // Vérifier que le taxateur existe
            Taxateur taxateur = taxateurRepository.findById(taxateurId)
                .orElse(null);
            
            if (taxateur == null) {
                return ResponseEntity.badRequest().body("Taxateur non trouvé");
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
            if (!passwordEncoder.matches(request.getCurrentPassword(), taxateur.getPassword())) {
                return ResponseEntity.badRequest().body("L'ancien mot de passe est incorrect");
            }

            // Encoder et sauvegarder le nouveau mot de passe
            taxateur.setPassword(passwordEncoder.encode(request.getNewPassword()));
            taxateurRepository.save(taxateur);

            return ResponseEntity.ok("Mot de passe modifié avec succès");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la modification du mot de passe: " + e.getMessage());
        }
    }

    /**
     * Récupère les informations du profil d'un taxateur
     */
    @GetMapping("/{taxateurId}/profile")
    public ResponseEntity<?> getTaxateurProfile(@PathVariable Long taxateurId) {
        try {
            Taxateur taxateur = taxateurRepository.findById(taxateurId)
                .orElse(null);
            
            if (taxateur == null) {
                return ResponseEntity.notFound().build();
            }

            // Retourner les informations du profil (sans le mot de passe)
            return ResponseEntity.ok(Map.of(
                "id", taxateur.getId(),
                "email", taxateur.getEmail(),
                "port", taxateur.getPort() != null ? taxateur.getPort().getNom() : "Non assigné",
                "portId", taxateur.getPort() != null ? taxateur.getPort().getId() : null,
                "createdAt", taxateur.getCreatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
