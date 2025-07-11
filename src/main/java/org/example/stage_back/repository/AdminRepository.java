package org.example.stage_back.repository;

import org.example.stage_back.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
} 