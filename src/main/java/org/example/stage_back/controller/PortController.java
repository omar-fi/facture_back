package org.example.stage_back.controller;

import org.example.stage_back.entities.Port;
import org.example.stage_back.repository.PortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ports")
public class PortController {

    @Autowired
    private PortRepository portRepository;

    @GetMapping
    public List<Port> getAllPorts() {
        return portRepository.findAll();
    }

    @PostMapping
    public Port createPort(@RequestBody Port port){
        return portRepository.save(port);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Port> updatePort(@PathVariable Long id, @RequestBody Port portDetails){
        return portRepository.findById(id)
                .map(port -> {
                    port.setNom(portDetails.getNom());
                    port.setVille(portDetails.getVille());
                    port.setTauxRK(portDetails.getTauxRK());
                    Port updatedPort = portRepository.save(port);
                    return ResponseEntity.ok(updatedPort);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePort(@PathVariable Long id){
        return portRepository.findById(id)
                .map(port -> {
                    portRepository.delete(port);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
} 