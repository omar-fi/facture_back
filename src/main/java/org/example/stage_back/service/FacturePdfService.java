package org.example.stage_back.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.entities.FactureDetail;
import org.example.stage_back.entities.Manifeste;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FacturePdfService {

    private static final Logger logger = LoggerFactory.getLogger(FacturePdfService.class);

    /**
     * Génère une facture PDF au format ANP
     */
    public byte[] genererFacturePdf(FactureEntete facture) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            
            // Configuration des polices
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            
            // En-tête de la facture
            dessinerEnTete(contentStream, facture);
            
            // Informations de la facture
            dessinerInfosFacture(contentStream, facture);
            
            // Informations du client (agent)
            dessinerInfosClient(contentStream, facture);
            
            // Tableau des lignes de facture
            dessinerTableauLignes(contentStream, facture);
            
            // Totaux
            dessinerTotaux(contentStream, facture);
            
            // Pied de page
            dessinerPiedDePage(contentStream);
            
            contentStream.close();
            
            // Convertir en bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            logger.error("Erreur lors de la génération du PDF", e);
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private void dessinerEnTete(PDPageContentStream contentStream, FactureEntete facture) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("AGENCE NATIONALE DES PORTS");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, 730);
        contentStream.showText("Direction des Opérations Portuaires");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 710);
        contentStream.showText("Service de Facturation");
        contentStream.endText();
    }

    private void dessinerInfosFacture(PDPageContentStream contentStream, FactureEntete facture) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 750);
        contentStream.showText("FACTURE");
        contentStream.endText();
        
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 730);
        contentStream.showText("N° Facture: F" + String.format("%06d", facture.getId()));
        contentStream.endText();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 710);
        contentStream.showText("Date: " + sdf.format(facture.getDateEmissionFact()));
        contentStream.endText();
    }

    private void dessinerInfosClient(PDPageContentStream contentStream, FactureEntete facture) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 650);
        contentStream.showText("CLIENT:");
        contentStream.endText();
        
        if (facture.getManifeste() != null && facture.getManifeste().getUser() != null) {
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 630);
            contentStream.showText("Agent: " + facture.getManifeste().getUser().getEmail());
            contentStream.endText();
        }
        
        if (facture.getManifeste() != null && facture.getManifeste().getPort() != null) {
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 610);
            contentStream.showText("Port: " + facture.getManifeste().getPort().getNom());
            contentStream.endText();
        }
    }

    private void dessinerTableauLignes(PDPageContentStream contentStream, FactureEntete facture) throws IOException {
        // En-tête du tableau
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 550);
        contentStream.showText("Désignation");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(300, 550);
        contentStream.showText("Montant HT");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 550);
        contentStream.showText("TVA");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(500, 550);
        contentStream.showText("Montant TTC");
        contentStream.endText();
        
        // Lignes de détail
        contentStream.setFont(PDType1Font.HELVETICA, 9);
        int y = 520;
        
        if (facture.getManifeste() != null && facture.getManifeste().getManifestLines() != null) {
            for (int i = 0; i < facture.getManifeste().getManifestLines().size() && i < 10; i++) {
                var line = facture.getManifeste().getManifestLines().get(i);
                
                contentStream.beginText();
                contentStream.newLineAtOffset(50, y);
                contentStream.showText(line.getMarchandise() != null ? line.getMarchandise() : "Marchandise " + (i + 1));
                contentStream.endText();
                
                y -= 20;
            }
        }
    }

    private void dessinerTotaux(PDPageContentStream contentStream, FactureEntete facture) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        
        // Calculer les totaux
        double totalHT = 0.0;
        double totalTVA = 0.0;
        double totalTTC = 0.0;
        
        if (facture.getManifeste() != null && facture.getManifeste().getMontantTotal() != null) {
            totalTTC = facture.getManifeste().getMontantTotal();
            totalHT = totalTTC / 1.20; // Supposons TVA 20%
            totalTVA = totalTTC - totalHT;
        }
        
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 200);
        contentStream.showText("Total HT: " + String.format("%.2f", totalHT) + " DH");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 180);
        contentStream.showText("TVA (20%): " + String.format("%.2f", totalTVA) + " DH");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(400, 160);
        contentStream.showText("Total TTC: " + String.format("%.2f", totalTTC) + " DH");
        contentStream.endText();
    }

    private void dessinerPiedDePage(PDPageContentStream contentStream) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 8);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 50);
        contentStream.showText("Cette facture est générée automatiquement par le système ANP");
        contentStream.endText();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 35);
        contentStream.showText("Pour toute question, veuillez contacter le service client");
        contentStream.endText();
    }
}
