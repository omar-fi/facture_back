package org.example.stage_back.controller;

import org.example.stage_back.entities.TarifStandard;
import org.example.stage_back.repository.TarifStandardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tarifs-standards")
public class TarifStandardController {

    @Autowired
    private TarifStandardRepository tarifStandardRepository;

    @GetMapping
    public List<TarifStandard> getAll() {
        return tarifStandardRepository.findAll();
    }

    @PostMapping
    public TarifStandard create(@RequestBody TarifStandard tarif) {
        return tarifStandardRepository.save(tarif);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifStandard> update(@PathVariable Integer id, @RequestBody TarifStandard details) {
        return tarifStandardRepository.findById(id)
                .map(tarif -> {
                    tarif.setPortId(details.getPortId());
                    tarif.setCategorie(details.getCategorie());
                    tarif.setLibelle(details.getLibelle());
                    tarif.setUnite(details.getUnite());
                    tarif.setTarifUnitaire(details.getTarifUnitaire());
                    tarif.setGroupName(details.getGroupName() != null ? details.getGroupName() : "Autre");
                    TarifStandard updated = tarifStandardRepository.save(tarif);
                    return ResponseEntity.ok(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return tarifStandardRepository.findById(id)
                .map(tarif -> {
                    tarifStandardRepository.delete(tarif);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Calcule le tarif ISPS selon la logique métier
     * Si le libellé contient "pleins", retourne 0, sinon retourne une valeur par défaut
     */
    private Double calculateTarifIsps(String libelle) {
        if (libelle != null && libelle.toLowerCase().contains("pleins")) {
            return 0.0;
        } else {
            // Vous pouvez modifier cette valeur par défaut selon vos besoins
            return 50.0; // Valeur par défaut pour les autres cas
        }
    }
} 