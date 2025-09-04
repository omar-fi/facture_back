package org.example.stage_back.service;

import jakarta.mail.MessagingException;
import org.example.stage_back.dto.FactureMailRequestDTO;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FactureApprobationService {

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureService factureService; // service mail

    // Méthode pour accepter la facture et envoyer le mail
    public void accepterFacture(Integer factureId) throws Exception {
        FactureEntete facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        factureRepository.save(facture);

        FactureMailRequestDTO mailRequest = new FactureMailRequestDTO();
        mailRequest.setFactureId(facture.getId().longValue());
        mailRequest.setEmailDestinataire(facture.getEscale().getAgent().getEmail());

        factureService.envoyerFactureParMail(mailRequest); // l'exception est propagée
    }

}
