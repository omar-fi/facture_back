package org.example.stage_back.repository;

import org.example.stage_back.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User , Long> {
Optional<User> findByEmail(String email);
}
