package org.example.stage_back.repository;

import org.example.stage_back.entities.ManifestLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManifestLineRepository extends JpaRepository<ManifestLine, Integer> {
} 