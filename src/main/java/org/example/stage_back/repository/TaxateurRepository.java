package org.example.stage_back.repository;

import org.example.stage_back.entities.Taxateur;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface TaxateurRepository extends JpaRepository<Taxateur, Long> {
} 