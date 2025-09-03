package org.example.stage_back.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.FactureDetail;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.entities.User;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Random;

@Service
public class FacturePdfService {

    // Méthode principale pour générer le PDF à partir d'un Agent et d'un montant
    public ByteArrayInputStream generateFacture(Agent agent, double montant) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();

            // 🔹 Titre facture
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("FACTURE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // 🔹 Générer numéro facture aléatoire
            int numeroFacture = new Random().nextInt(999999);

            // 🔹 Infos facture
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Numéro de facture : " + numeroFacture, normalFont));
            document.add(new Paragraph("Date : " + LocalDate.now(), normalFont));
            document.add(new Paragraph(" "));

            // 🔹 Infos Agent (client)
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            document.add(new Paragraph("Informations Client (Agent)", boldFont));

            document.add(new Paragraph("Raison sociale : " + agent.getRaisonSociale(), normalFont));
            document.add(new Paragraph("Port demandé : " + agent.getPortDemande(), normalFont));
            document.add(new Paragraph("ICE : " + agent.getICE(), normalFont));
            document.add(new Paragraph("Port : " + (agent.getPort() != null ? agent.getPort().toString() : "N/A"), normalFont));
            document.add(new Paragraph(" "));

            // 🔹 Montant
            document.add(new Paragraph("Montant Total : " + montant + " MAD", boldFont));

            document.close();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream generateFacturePdf(FactureEntete factureEntete) {
        if (factureEntete.getManifeste() == null) {
            throw new RuntimeException("FactureEntete n'a pas de manifeste associé");
        }

        // Récupérer le User et le caster en Agent
        User user = factureEntete.getManifeste().getUser();
        if (!(user instanceof Agent)) {
            throw new RuntimeException("Le user lié au manifeste n'est pas un agent");
        }
        Agent agent = (Agent) user;

        // Calculer le montant total à partir de montantTTC
        double montant = 0.0;
        if (factureEntete.getDetails() != null) {
            for (FactureDetail detail : factureEntete.getDetails()) {
                if (detail.getMontantTTC() != null) {
                    montant += detail.getMontantTTC().doubleValue();
                }
            }
        }

        return generateFacture(agent, montant);
    }


}
