package org.example.stage_back.repository;

import org.example.stage_back.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
 
public interface AgentRepository extends JpaRepository<Agent, Long> {
    boolean existsByEmail(String email);
    Optional<Agent> findByEmail(String email);
} 