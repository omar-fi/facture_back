package org.example.stage_back.controller;

import org.example.stage_back.dto.AgentInscriptionRequest;
import org.example.stage_back.entities.AgentInscrit;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.User;
import org.example.stage_back.repository.AgentInscritRepository;
import org.example.stage_back.repository.UserRepo;
import org.example.stage_back.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;

@RestController
public class AgentInscritController {
    @Autowired
    private AgentInscritRepository agentInscritRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AgentRepository agentRepository;


    @PostMapping("/register")
    public ResponseEntity<AgentInscrit> register(@RequestBody AgentInscriptionRequest request) {
        AgentInscrit agent = new AgentInscrit();
        agent.setEmail(request.getEmail());
        agent.setRaisonSociale(request.getRaisonSociale());
        agent.setPortDemande(request.getPortDemande());
        agent.setICE(request.getIce());
        agent.setPassword(request.getPassword());
        agent.setStatut(AgentInscrit.Statut.EN_ATTENTE);
        agent.setDateInscription(new Date());
        AgentInscrit saved = agentInscritRepository.save(agent);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/admin/agents-inscrits")
    public ResponseEntity<?> getAllAgentsInscrits() {
        return ResponseEntity.ok(agentInscritRepository.findAll()
            .stream()
            .filter(a -> a.getStatut() == AgentInscrit.Statut.EN_ATTENTE)
            .toList());
    }


    @PostMapping("/admin/agents-inscrits/{id}/accepter")
    public ResponseEntity<?> accepterAgentInscrit(@PathVariable Integer id) {
        return agentInscritRepository.findById(id)
            .map(agentInscrit -> {

                Agent agent = new Agent();
                agent.setEmail(agentInscrit.getEmail());
                agent.setPassword(agentInscrit.getPassword());
                agent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                agent.setRaisonSociale(agentInscrit.getRaisonSociale());
                agent.setPortDemande(agentInscrit.getPortDemande());
                agent.setICE(agentInscrit.getICE() != null ? agentInscrit.getICE().longValue() : null);

                agentRepository.save(agent);

                agentInscritRepository.delete(agentInscrit);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }

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