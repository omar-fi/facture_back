package org.example.stage_back.repository;

import org.example.stage_back.entities.Taxateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
 
public interface TaxateurRepository extends JpaRepository<Taxateur, Long> {
    boolean existsByEmail(String email);
    Optional<Taxateur> findByEmail(String email);
} 