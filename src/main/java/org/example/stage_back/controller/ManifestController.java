package org.example.stage_back.controller;

import org.example.stage_back.service.ManifestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/manifest")
public class ManifestController {

    @Autowired
    private ManifestService manifestService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadManifest(@RequestParam("file") MultipartFile file) {
        try {
            manifestService.parseAndSaveManifest(file, file.getOriginalFilename());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }
}