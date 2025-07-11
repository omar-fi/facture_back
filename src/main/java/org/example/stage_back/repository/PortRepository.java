package org.example.stage_back.repository;

import org.example.stage_back.entities.Port;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface PortRepository extends JpaRepository<Port, Integer> {
} 