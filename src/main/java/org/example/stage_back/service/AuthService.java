package org.example.stage_back.service;

import org.example.stage_back.dto.LoginRequest;
import org.example.stage_back.entities.User;
import org.example.stage_back.repository.UserRepo;
import org.example.stage_back.repository.AdminRepository;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.repository.TaxateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private TaxateurRepository taxateurRepository;
    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    public String authenticate(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));


        if (passwordEncoder != null) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            if (!password.equals(user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
        }

        if (adminRepository.existsById(user.getId())) {
            return "ADMIN";
        } else if (agentRepository.existsById(user.getId())) {
            return "AGENT";
        } else if (taxateurRepository.existsById(user.getId())) {
            return "TAXATEUR";
        }
        return "UNKNOWN";
    }

} 