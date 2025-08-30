package org.example.stage_back.repository;

import org.example.stage_back.entities.FactureDetail;
import org.example.stage_back.entities.FactureEntete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FactureDetailRepository extends JpaRepository<FactureDetail, Integer> {
    List<FactureDetail> findByFactureEntete(FactureEntete factureEntete);
} 