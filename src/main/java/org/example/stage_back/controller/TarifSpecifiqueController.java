package org.example.stage_back.controller;

import org.example.stage_back.entities.TarifSpecifique;
import org.example.stage_back.repository.TarifSpecifiqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tarifs-specifiques")
public class TarifSpecifiqueController {

    @Autowired
    private TarifSpecifiqueRepository tarifSpecifiqueRepository;

    @GetMapping
    public List<TarifSpecifique> getAll() {
        return tarifSpecifiqueRepository.findAll();
    }

    @PostMapping
    public TarifSpecifique create(@RequestBody TarifSpecifique tarif) {
        return tarifSpecifiqueRepository.save(tarif);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifSpecifique> update(@PathVariable Integer id, @RequestBody TarifSpecifique details) {
        return tarifSpecifiqueRepository.findById(id)
                .map(tarif -> {
                    tarif.setPortId(details.getPortId());
                    tarif.setCategorie(details.getCategorie());
                    tarif.setLibelle(details.getLibelle());
                    tarif.setUnite(details.getUnite());
                    tarif.setTarifUnitaire(details.getTarifUnitaire());
                    tarif.setGroupName(details.getGroupName() != null ? details.getGroupName() : "Autre");
                    TarifSpecifique updated = tarifSpecifiqueRepository.save(tarif);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return tarifSpecifiqueRepository.findById(id)
                .map(tarif -> {
                    tarifSpecifiqueRepository.delete(tarif);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
} 