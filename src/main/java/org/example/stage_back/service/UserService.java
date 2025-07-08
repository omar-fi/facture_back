package org.example.stage_back.service;

import org.example.stage_back.entities.AgentInscrit;
import org.example.stage_back.entities.User;
import org.example.stage_back.entities.Role;
import org.example.stage_back.repository.AgentInscritRepo;
import org.example.stage_back.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AgentInscritRepo agentInscritRepo;

    public AgentInscrit registerUser(AgentInscrit user){
        user.setRole(Role.Agent);
        return agentInscritRepo.save(user);
    }
} 