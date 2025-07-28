package org.example.stage_back.controller;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.dto.TaxateurRequestDTO;
import org.example.stage_back.dto.UserResponseDTO;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.Port;
import org.example.stage_back.entities.Taxateur;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.repository.PortRepository;
import org.example.stage_back.repository.TaxateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/admin/utilisateurs")
@RequiredArgsConstructor
public class UserManagementController {

    private final AgentRepository agentRepository;
    private final TaxateurRepository taxateurRepository;
    private final PortRepository portRepository;

    // ✅ 1. Obtenir tous les utilisateurs
// Méthode de récupération des utilisateurs
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> agents = agentRepository.findAll().stream().map(agent -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(Math.toIntExact(agent.getId()));
            dto.setEmail(agent.getEmail());
            dto.setRole("AGENT");

            // Vérification que l'agent a un port
            if (agent.getPort() != null) {
                dto.setPortNom(agent.getPort().getNom());
                dto.setPortId(agent.getPort() != null ? Long.valueOf(agent.getPort().getId()) : null);
            } else {
                dto.setPortNom("Aucun port");
                dto.setPortId(null);  // Ou vous pouvez attribuer un ID par défaut
            }

            return dto;
        }).toList();

        List<UserResponseDTO> taxateurs = taxateurRepository.findAll().stream().map(tax -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(Math.toIntExact(tax.getId()));
            dto.setEmail(tax.getEmail());
            dto.setRole("TAXATEUR");

            // Vérification que le taxateur a un port
            if (tax.getPort() != null) {
                dto.setPortNom(tax.getPort().getNom());
                dto.setPortId(tax.getPort() != null ? Long.valueOf(tax.getPort().getId()) : null);
            } else {
                dto.setPortNom("Aucun port");
                dto.setPortId(null);  // Ou vous pouvez attribuer un ID par défaut
            }

            return dto;
        }).toList();

        return Stream.concat(agents.stream(), taxateurs.stream()).toList();
    }

    // ✅ 2. Supprimer un agent
    @DeleteMapping("/agent/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable Long id) {
        if (!agentRepository.existsById(id)) return ResponseEntity.notFound().build();
        agentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ✅ 3. Supprimer un taxateur
    @DeleteMapping("/taxateur/{id}")
    public ResponseEntity<?> deleteTaxateur(@PathVariable Long id) {
        if (!taxateurRepository.existsById(id)) return ResponseEntity.notFound().build();
        taxateurRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ✅ 4. Créer un taxateur
    @PostMapping("/taxateur")
    public ResponseEntity<?> createTaxateur(@RequestBody TaxateurRequestDTO request) {
        Port port = portRepository.findById(Math.toIntExact(request.getPortId())).orElseThrow();
        Taxateur taxateur = new Taxateur();
        taxateur.setEmail(request.getEmail());
        taxateur.setPassword(request.getPassword()); // À encoder si sécurité activée
        taxateur.setPort(port);
        taxateurRepository.save(taxateur);
        return ResponseEntity.ok().build();
    }

    // ✅ 5. Modifier un taxateur
    @PutMapping("/taxateur/{id}")
    public ResponseEntity<?> updateTaxateur(@PathVariable Long id, @RequestBody TaxateurRequestDTO request) {
        Taxateur taxateur = taxateurRepository.findById(id).orElseThrow();
        Port port = portRepository.findById(Math.toIntExact(request.getPortId())).orElseThrow();
        taxateur.setEmail(request.getEmail());
        taxateur.setPort(port);
        taxateurRepository.save(taxateur);
        return ResponseEntity.ok().build();
    }
}