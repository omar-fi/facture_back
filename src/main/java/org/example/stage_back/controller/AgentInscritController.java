package org.example.stage_back.controller;

import org.example.stage_back.dto.AgentInscriptionRequest;
import org.example.stage_back.entities.AgentInscrit;
import org.example.stage_back.repository.AgentInscritRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/register")
public class AgentInscritController {
    @Autowired
    private AgentInscritRepository agentInscritRepository;

    @PostMapping
    public ResponseEntity<AgentInscrit> register(@RequestBody AgentInscriptionRequest request) {
        AgentInscrit agent = new AgentInscrit();
        agent.setFullName(request.getFullName());
        agent.setEmail(request.getEmail());
        agent.setRaisonSociale(request.getRaisonSociale());
        agent.setPortDemande(request.getPortDemande());
        agent.setICE(request.getICE());
        agent.setPassword(request.getPassword());
        agent.setStatut(AgentInscrit.Statut.EN_ATTENTE);
        agent.setDateInscription(new Date());
        AgentInscrit saved = agentInscritRepository.save(agent);
        return ResponseEntity.ok(saved);
    }
} 