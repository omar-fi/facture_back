package org.example.stage_back.repository;

import org.example.stage_back.entities.Correspondance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorrespondanceRepository extends JpaRepository<Correspondance, Integer> {
    // Correct: retourne un Optional<Correspondance> et prend un Integer
    Optional<Correspondance> findByCodeSH(Integer codeSH);
}
