package org.example.stage_back.controller;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.example.stage_back.entities.Categories;
import org.example.stage_back.entities.ManifestLine;
import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.jar.Manifest;

@RestController
@RequestMapping("/admin/categories")
public class CategoriesController {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping
    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    @PostMapping
    public Categories createCategory(@RequestBody Categories category) {
        return categoriesRepository.save(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategory(@PathVariable Integer id, @RequestBody Categories categoryDetails) {
        return categoriesRepository.findById(id)
                .map(category -> {
                    category.setCategorie(categoryDetails.getCategorie());
                    category.setLibelle(categoryDetails.getLibelle());
                    category.setUnite(categoryDetails.getUnite());
                    Categories updatedCategory = categoriesRepository.save(category);
                    return ResponseEntity.ok(updatedCategory);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        return categoriesRepository.findById(id)
                .map(category -> {
                    categoriesRepository.delete(category);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
} 