package org.example.stage_back.repository;

import org.example.stage_back.entities.FactureEntete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureEnteteRepository extends JpaRepository<FactureEntete, Long> {
    List<FactureEntete> findByManifeste_User_Id(Long userId);
} 