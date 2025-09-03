package org.example.stage_back.service;

import jakarta.mail.MessagingException;
import org.example.stage_back.entities.FactureEntete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String defaultFromAddress;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }



    public void sendAccountValidationEmail(String toEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Validation de votre compte ANP");
            helper.setText(buildHtmlMessage(), true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildHtmlMessage() {
        return """
        <html>
        <body style="font-family: Arial; padding: 20px;">
          <h2 style="color:#007BFF;">Votre compte a été validé avec succès !</h2>
          <p>Bonjour,</p>
          <p>Votre compte d’agent maritime a été activé par l’administrateur.</p>
          <p>Vous pouvez maintenant accéder à votre espace dédié sur notre plateforme.</p>
          <p style="text-align: center; margin: 30px;">
            <a href="http://localhost:5173/agent"
               style="background-color:#28a745; color:white; padding:12px 20px; text-decoration:none; border-radius:5px;">
              Accéder à mon espace
            </a>
          </p>
          <p>Cordialement,<br>L’équipe de stage ANP</p>
        </body>
        </html>
        """;
    }
    /**
     * Envoie un email de validation de compte à un agent
     */


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
        // Simulation d'envoi d'email
        logger.info("Email de notification envoyé pour la facture: {}", facture.getId());

        // Ici vous pourriez implémenter l'envoi d'email réel
        // Par exemple avec JavaMailSender ou un service externe
    }
}