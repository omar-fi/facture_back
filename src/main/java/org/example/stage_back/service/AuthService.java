package org.example.stage_back.service;

import org.example.stage_back.dto.LoginRequest;
import org.example.stage_back.entities.Admin;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.Taxateur;
import org.example.stage_back.repository.AdminRepository;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.repository.TaxateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private TaxateurRepository taxateurRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String email, String password) {
        // Test avec Admin
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            // Vérifier si le mot de passe est encodé en BCrypt ou en clair
            if (passwordEncoder.matches(password, admin.getPassword()) || password.equals(admin.getPassword())) {
                return "ADMIN";
            }
        }

        // Test avec Agent
        Agent agent = agentRepository.findByEmail(email).orElse(null);
        if (agent != null) {
            // Vérifier si le mot de passe est encodé en BCrypt ou en clair
            if (passwordEncoder.matches(password, agent.getPassword()) || password.equals(agent.getPassword())) {
                return "AGENT";
            }
        }

        // Test avec Taxateur
        Taxateur taxateur = taxateurRepository.findByEmail(email).orElse(null);
        if (taxateur != null) {
            // Vérifier si le mot de passe est encodé en BCrypt ou en clair
            if (passwordEncoder.matches(password, taxateur.getPassword()) || password.equals(taxateur.getPassword())) {
                return "TAXATEUR";
            }
        }

        throw new RuntimeException("Email ou mot de passe incorrect");
    }
} 