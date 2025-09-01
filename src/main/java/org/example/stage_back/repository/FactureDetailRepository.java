package org.example.stage_back.repository;

import org.example.stage_back.entities.FactureDetail;
import org.example.stage_back.entities.FactureEntete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureDetailRepository extends JpaRepository<FactureDetail, Long> {
    List<FactureDetail> findByFactureEntete(FactureEntete factureEntete);
} 