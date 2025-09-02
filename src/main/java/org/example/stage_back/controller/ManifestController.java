package org.example.stage_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.service.ManifestService;
import org.example.stage_back.repository.ManifesteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.stage_back.dto.ManifestDTO;

import java.util.List;

@RestController
@RequestMapping("/api/manifest")
@RequiredArgsConstructor
public class ManifestController {

    private final ManifestService manifestService;
    private final ManifesteRepository manifesteRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadManifest(@RequestParam("file") MultipartFile file, @RequestParam("agentId") Long agentId) {
        try {
            manifestService.parseAndSaveManifest(file, file.getOriginalFilename(), agentId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace(); // <--- ajoute ça
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            manifesteRepository.deleteById(Long.valueOf(id));
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping
    public List<ManifestDTO> list() {
        return manifesteRepository.findAllListItems();
    }




}