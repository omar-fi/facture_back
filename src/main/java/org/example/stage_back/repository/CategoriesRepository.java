package org.example.stage_back.repository;

import org.example.stage_back.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
} 