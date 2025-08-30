package org.example.stage_back.controller;

import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.entities.Manifeste;
import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.service.ManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/manifest")
public class ManifestController {

    @Autowired
    private ManifestService manifestService;

    @Autowired
    private ManifesteRepository manifesteRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadManifest(
            @RequestParam("file") MultipartFile file,
            @RequestParam("agentId") Long agentId) {
        try {
            manifestService.parseAndSaveManifest(file, file.getOriginalFilename(), agentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ManifestDTO>> getAllManifests() {
        try {
            List<Manifeste> manifests = manifesteRepository.findAll();
            List<ManifestDTO> manifestDTOs = manifests.stream()
                .map(ManifestDTO::fromEntity)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
            return ResponseEntity.ok(manifestDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}