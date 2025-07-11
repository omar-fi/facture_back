package org.example.stage_back.repository;

import org.example.stage_back.entities.FactureDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureDetailRepository extends JpaRepository<FactureDetail, Integer> {
} 