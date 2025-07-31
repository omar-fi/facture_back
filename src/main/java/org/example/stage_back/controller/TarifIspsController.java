package org.example.stage_back.controller;

import org.example.stage_back.entities.TarifIsps;
import org.example.stage_back.repository.TarifIspsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tarifs-isps")
public class TarifIspsController {

    @Autowired
    private TarifIspsRepository tarifIspsRepository;

    @GetMapping
    public List<TarifIsps> getAll() {
        return tarifIspsRepository.findAll();
    }

    @PostMapping
    public TarifIsps create(@RequestBody TarifIsps tarifIsps) {
        return tarifIspsRepository.save(tarifIsps);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifIsps> update(@PathVariable Integer id, @RequestBody TarifIsps details) {
        return tarifIspsRepository.findById(id)
                .map(tarif -> {
                    tarif.setLibelle(details.getLibelle());
                    tarif.setTarif(details.getTarif());
                    TarifIsps updated = tarifIspsRepository.save(tarif);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return tarifIspsRepository.findById(id)
                .map(tarif -> {
                    tarifIspsRepository.delete(tarif);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
} 