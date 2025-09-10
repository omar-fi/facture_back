package org.example.stage_back.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.stage_back.dto.EmailRequest;
import org.example.stage_back.dto.FactureDetailDTO;
import org.example.stage_back.dto.FactureLigneDTO;
import org.example.stage_back.dto.FactureMailRequestDTO;
import org.example.stage_back.entities.*;
import org.example.stage_back.repository.CorrespondanceRepository;
import org.example.stage_back.repository.FactureEnteteRepository;
import org.example.stage_back.repository.ManifesteRepository;
import org.example.stage_back.repository.TarifStandardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FactureService {

    private final FacturePdfService facturePdfService;
    private final JavaMailSender mailSender;
    private final FactureEnteteRepository factureEnteteRepository;
    private final ManifesteRepository manifesteRepository;
    private final TarifStandardRepository tarifStandardRepository;
    private final CorrespondanceRepository correspondanceRepository;

    @Autowired
    public FactureService(FacturePdfService facturePdfService,
                          JavaMailSender mailSender,
                          FactureEnteteRepository factureEnteteRepository,
                          ManifesteRepository manifesteRepository,
                          TarifStandardRepository tarifStandardRepository,
                          CorrespondanceRepository correspondanceRepository) {
        this.facturePdfService = facturePdfService;
        this.mailSender = mailSender;
        this.factureEnteteRepository = factureEnteteRepository;
        this.manifesteRepository = manifesteRepository;
        this.tarifStandardRepository = tarifStandardRepository;
        this.correspondanceRepository = correspondanceRepository;
    }

    // 🔹 Calculer facture
    public FactureDetailDTO calculerFacture(Agent agent, Manifeste manifeste) {
        if (manifeste.getManifestLines() == null || manifeste.getManifestLines().isEmpty()) {
            throw new RuntimeException("⚠️ Manifeste vide");
        }

        Map<String, FactureLigneDTO> lignesMap = new HashMap<>();
        double totalHT = 0.0;

        Long portId = (agent.getPort() != null ? agent.getPort().getId() : null);

        for (ManifestLine ligne : manifeste.getManifestLines()) {
            String libelleMH = ligne.getLibellemh() != null ? ligne.getLibellemh() : "N/A";

            // ---- 1. Catégorie via correspondance
            String categorie = null;
            if (ligne.getCodesh() != null) {
                categorie = correspondanceRepository.findByCodeSH(ligne.getCodesh())
                        .map(Correspondance::getCategorie)
                        .orElse(null);
            }

            // ---- 2. Unité + tarif
            String unite = "N/A";
            Double tarif = 0.0;
            if (categorie != null && portId != null) {
                List<TarifStandard> list = tarifStandardRepository.findByCategorie(categorie);
                TarifStandard ts = list.isEmpty() ? null : list.get(0);

                if (ts != null) {
                    tarif = ts.getTarifUnitaire();
                    unite = ts.getUnite().name();
                }
            }

            // ---- 3. Quantité
            double quantite = (ligne.getPoids() != null && ligne.getPoids() > 0)
                    ? ligne.getPoids()
                    : 1.0;

            // ---- 4. Montants
            double montantHT = tarif * quantite;
            double montantTR = (agent.getPort() != null && agent.getPort().getTauxRK() != null)
                    ? montantHT * agent.getPort().getTauxRK()
                    : 0.0;
            double montantTVA = montantHT * 0.20;
            double montantTTC = montantHT + montantTR + montantTVA;

            // ---- 5. Debug log pour chaque ligne traitée
            System.out.println("➡️ Ligne traitée : codesh=" + ligne.getCodesh()
                    + ", categorie=" + categorie
                    + ", tarif=" + tarif
                    + ", quantite=" + quantite
                    + ", montantHT=" + montantHT);

            // ---- 6. Agrégation
            String key = (categorie != null ? categorie : "NA") + "-" + libelleMH + "-" + unite;
            if (lignesMap.containsKey(key)) {
                FactureLigneDTO existing = lignesMap.get(key);
                existing.setQuantite(existing.getQuantite() + quantite);
                existing.setRepetitions(existing.getRepetitions() + 1);
                existing.setMontantHT(existing.getMontantHT() + montantHT);
            } else {
                lignesMap.put(key, new FactureLigneDTO(libelleMH, unite, quantite, montantHT, 1L, tarif));
            }

            totalHT += montantHT;
        }

        // ---- Totaux généraux
        double totalTR = (agent.getPort() != null && agent.getPort().getTauxRK() != null)
                ? totalHT * agent.getPort().getTauxRK()
                : 0.0;
        double totalTVA = (totalHT + totalTR) * 0.20;
        double totalTTC = totalHT + totalTR + totalTVA;

        // ---- Debug log global
        System.out.println("🔎 Lignes générées : " + lignesMap.values());

        FactureDetailDTO dto = new FactureDetailDTO(totalHT, totalTR, totalTVA, totalTTC);
        dto.setLignes(new ArrayList<>(lignesMap.values()));

        return dto;
    }


    // 🔹 Envoi email avec PDF en Base64
    public void envoyerEmailAvecPdf(EmailRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(request.getEmail());
        helper.setSubject(request.getSubject());
        helper.setText(request.getMessage());

        byte[] pdfBytes = java.util.Base64.getDecoder().decode(request.getPdfBase64());
        helper.addAttachment(request.getFilename(), new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }

    // 🔹 Envoi facture par mail (depuis DB)
    public void envoyerFactureParMail(FactureMailRequestDTO request) throws Exception {
        FactureEntete facture = factureEnteteRepository.findById(request.getFactureId())
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        ByteArrayInputStream pdfStream = facturePdfService.generateFacturePdf(facture);
        byte[] pdfBytes = pdfStream.readAllBytes();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(request.getEmailDestinataire());
        helper.setSubject("Facture #" + facture.getId());
        helper.setText("Bonjour,\n\nVeuillez trouver votre facture en pièce jointe.\n\nCordialement.");
        helper.addAttachment("facture.pdf", new ByteArrayResource(pdfBytes));

        mailSender.send(message);
    }
}

