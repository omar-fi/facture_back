package org.example.stage_back.repository;

import org.example.stage_back.entities.Correspondance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrespondanceRepository extends JpaRepository<Correspondance, Integer> {
} 