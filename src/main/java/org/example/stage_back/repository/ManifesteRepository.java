package org.example.stage_back.repository;

import org.example.stage_back.entities.Manifeste;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface ManifesteRepository extends JpaRepository<Manifeste, Integer> {
} 