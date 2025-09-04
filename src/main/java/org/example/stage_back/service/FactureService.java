package org.example.stage_back.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.stage_back.dto.EmailRequest;
import org.example.stage_back.dto.FactureMailRequestDTO;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class FactureService {

    private final FacturePdfService facturePdfService;
    private final JavaMailSender mailSender;
    private final FactureEnteteRepository factureEnteteRepository;
    // Dans FactureService.java
    public void envoyerEmailAvecPdf(EmailRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(request.getEmail()); // Utilise le champ 'email' directement
        helper.setSubject(request.getSubject());
        helper.setText(request.getMessage());

        // Convertit la chaîne Base64 en tableau de bytes pour la pièce jointe
        byte[] pdfBytes = java.util.Base64.getDecoder().decode(request.getPdfBase64());
        helper.addAttachment(request.getFilename(), new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
    @Autowired
    public FactureService(FacturePdfService facturePdfService,
                          JavaMailSender mailSender,
                          FactureEnteteRepository factureEnteteRepository) {
        this.facturePdfService = facturePdfService;
        this.mailSender = mailSender;
        this.factureEnteteRepository = factureEnteteRepository;
    }

    public void envoyerFactureParMail(FactureMailRequestDTO request) throws Exception {
        // 1️⃣ Récupérer la facture
        FactureEntete facture = factureEnteteRepository.findById(request.getFactureId())
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        // 2️⃣ Générer le PDF
        ByteArrayInputStream pdfStream = facturePdfService.generateFacturePdf(facture);
        byte[] pdfBytes = pdfStream.readAllBytes();

        // 3️⃣ Préparer l'email avec pièce jointe
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart
        helper.setTo(request.getEmailDestinataire());
        helper.setSubject("Facture #" + facture.getId());
        helper.setText("Bonjour,\n\nVeuillez trouver votre facture en pièce jointe.\n\nCordialement.");
        helper.addAttachment("facture.pdf", new ByteArrayResource(pdfBytes));

        // 4️⃣ Envoyer l'email
        mailSender.send(message);
    }

}
