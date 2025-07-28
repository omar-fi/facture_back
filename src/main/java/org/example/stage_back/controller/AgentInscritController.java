package org.example.stage_back.controller;

import org.example.stage_back.dto.AgentInscriptionRequest;
import org.example.stage_back.entities.AgentInscrit;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.service.EmailService;
import org.example.stage_back.entities.Port;
import org.example.stage_back.repository.AgentInscritRepository;
import org.example.stage_back.repository.PortRepository;
import org.example.stage_back.repository.UserRepo;
import org.example.stage_back.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AgentInscritController {

    private final AgentInscritRepository agentInscritRepository;
    private final AgentRepository agentRepository;
    private final PortRepository portRepository;
    private final UserRepo userRepo; // utilisé plus tard peut-être

    // ✅ Enregistrement d’un agent en attente
// ✅ Enregistrement d’un agent en attente sans traitement du port
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AgentInscriptionRequest request) {
        // Validation de l'ICE : Vérification qu'il s'agit d'un nombre à 15 chiffres


        // Encoder le mot de passe
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Créer l'agent et remplir les informations
        AgentInscrit agent = new AgentInscrit();
        agent.setEmail(request.getEmail());
        agent.setRaisonSociale(request.getRaisonSociale());
        agent.setICE(request.getIce());
        agent.setPassword(encodedPassword); // Mot de passe encodé
        agent.setStatut(AgentInscrit.Statut.EN_ATTENTE);
        agent.setDateInscription(new Date());

        // Enregistrer l'agent dans la base de données sans traitement du port
        agentInscritRepository.save(agent);

        // Réponse avec un message de succès
        return ResponseEntity.ok("Inscription enregistrée avec succès pour l'agent ID " + agent.getId());
    }


    // ✅ Liste des agents en attente
    @GetMapping("/admin/agents-inscrits")
    public ResponseEntity<?> getAllAgentsInscrits() {
        return ResponseEntity.ok(
                agentInscritRepository.findAll()
                        .stream()
                        .filter(a -> a.getStatut() == AgentInscrit.Statut.EN_ATTENTE)
                        .toList()
        );
    }
    @Autowired
    private EmailService emailService;
    @PostMapping("/admin/agents-inscrits/{id}/accepter")
    public ResponseEntity<?> accepterAgentInscrit(@PathVariable Integer id) {
        try {
            Optional<AgentInscrit> optional = agentInscritRepository.findById(id);
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent introuvable");
            }

            AgentInscrit agentInscrit = optional.get();

            // Vérifie si l’email existe déjà dans la table User
            if (userRepo.findByEmail(agentInscrit.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Un utilisateur avec cet email existe déjà.");
            }

            // Créer l'agent
            Agent agent = new Agent();
            agent.setEmail(agentInscrit.getEmail());
            agent.setPassword(agentInscrit.getPassword());
            agent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            agent.setRaisonSociale(agentInscrit.getRaisonSociale());
            agent.setPortDemande(agentInscrit.getPortDemande() != null ? agentInscrit.getPortDemande().toString() : null);
            agent.setICE(agentInscrit.getICE() != null ? agentInscrit.getICE().longValue() : null);

            agentRepository.save(agent);
            agentInscritRepository.delete(agentInscrit);

            emailService.sendAccountValidationEmail(agent.getEmail());

            return ResponseEntity.ok("Agent accepté et email envoyé.");

        } catch (Exception e) {
            e.printStackTrace(); // log terminal
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }


    // ✅ Supprimer une demande d'inscription
    @DeleteMapping("/admin/agents-inscrits/{id}")
    public ResponseEntity<?> supprimerAgentInscrit(@PathVariable Integer id) {
        if (agentInscritRepository.existsById(id)) {
            agentInscritRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
