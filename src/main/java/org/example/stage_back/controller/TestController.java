package org.example.stage_back.controller;

import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.repository.AgentInscritRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private FactureEnteteRepository factureEnteteRepository;

    @Autowired
    private AgentInscritRepository agentInscritRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World! API is working!";
    }

    @GetMapping("/manifests/count")
    public String getManifestCount() {
        try {
            long count = manifesteRepository.count();
            return "Nombre total de manifests: " + count;
        } catch (Exception e) {
            return "Erreur: " + e.getMessage();
        }
    }

    @GetMapping("/factures/count")
    public String getFactureCount() {
        try {
            long count = factureEnteteRepository.count();
            return "Nombre total de factures: " + count;
        } catch (Exception e) {
            return "Erreur: " + e.getMessage();
        }
    }

    @GetMapping("/agents/count")
    public String getAgentCount() {
        try {
            long count = agentInscritRepository.count();
            return "Nombre total d'agents inscrits: " + count;
        } catch (Exception e) {
            return "Erreur: " + e.getMessage();
        }
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        try {
            status.put("manifests", manifesteRepository.count());
            status.put("factures", factureEnteteRepository.count());
            status.put("agents", agentInscritRepository.count());
            status.put("status", "OK");
            status.put("message", "API fonctionne correctement");
        } catch (Exception e) {
            status.put("status", "ERROR");
            status.put("message", e.getMessage());
        }
        return status;
    }
}
