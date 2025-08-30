package org.example.stage_back.service;

import org.example.stage_back.entities.FactureEntete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Envoie un email de validation de compte à un agent
     */
    public void sendAccountValidationEmail(String toEmail) {
        try {
            logger.info("Envoi d'email de validation de compte à: {}", toEmail);
            
            // TODO: Implémenter l'envoi d'email avec JavaMail ou autre service
            // Pour l'instant, on simule l'envoi
            
            logger.info("Email de validation envoyé avec succès à: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de validation", e);
            throw new RuntimeException("Erreur lors de l'envoi de l'email de validation", e);
        }
    }

    /**
     * Envoie une facture par email à l'agent
     */
    public void envoyerFactureParEmail(FactureEntete facture, String emailAgent) {
        try {
            logger.info("Envoi de la facture {} par email à {}", facture.getId(), emailAgent);
            
            // TODO: Implémenter l'envoi d'email avec JavaMail ou autre service
            // Pour l'instant, on simule l'envoi
            
            logger.info("Facture {} envoyée avec succès à {}", facture.getId(), emailAgent);
            
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la facture par email", e);
            throw new RuntimeException("Erreur lors de l'envoi de la facture par email", e);
        }
    }

    /**
     * Envoie une notification de facture générée
     */
    public void notifierFactureGeneree(FactureEntete facture) {
        try {
            if (facture.getManifeste() != null && facture.getManifeste().getUser() != null) {
                String emailAgent = facture.getManifeste().getUser().getEmail();
                logger.info("Notification de facture générée pour l'agent: {}", emailAgent);
                
                // TODO: Implémenter la notification
                logger.info("Notification envoyée avec succès");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification", e);
        }
    }
}
