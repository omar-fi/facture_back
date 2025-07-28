package org.example.stage_back.repository;

import org.example.stage_back.entities.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface PortRepository extends JpaRepository<Port, Long> {
        // rien à ajouter ici, findById() est hérité
    }

