package org.example.stage_back.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

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
}
