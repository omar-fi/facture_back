package org.example.stage_back.service;

import org.example.stage_back.entities.FactureEntete;
import org.example.stage_back.entities.FactureDetail;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class FacturePdfService {

    public Resource generateFacturePdf(FactureEntete facture) throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);

        document.open();

        // En-tête
        addHeader(document, facture);
        
        // Informations de la facture
        addInvoiceInfo(document, facture);
        
        // Détails de la facture
        addInvoiceDetails(document, facture);
        
        // Total
        addTotal(document, facture);

        document.close();
        
        return new ByteArrayResource(baos.toByteArray());
    }

    private void addHeader(Document document, FactureEntete facture) throws DocumentException {
        // Logo ou titre
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("FACTURE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        document.add(new Paragraph(" ")); // Espace
    }

    private void addInvoiceInfo(Document document, FactureEntete facture) throws DocumentException {
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        // Informations de base
        document.add(new Paragraph("Numéro de facture: " + facture.getId(), boldFont));
        document.add(new Paragraph("Date d'émission: " + sdf.format(facture.getDateEmissionFact()), normalFont));
        
        if (facture.getManifeste() != null) {
            document.add(new Paragraph("Manifest: " + facture.getManifeste().getId(), normalFont));
        }
        
        if (facture.getEscale() != null && facture.getEscale().getPort() != null) {
            document.add(new Paragraph("Port: " + facture.getEscale().getPort().getNom(), normalFont));
        }
        
        document.add(new Paragraph(" ")); // Espace
    }

    private void addInvoiceDetails(Document document, FactureEntete facture) throws DocumentException {
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        
        // En-tête du tableau
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        
        table.addCell(new PdfPCell(new Phrase("Description", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Montant HT", boldFont)));
        table.addCell(new PdfPCell(new Phrase("TVA", boldFont)));
        table.addCell(new PdfPCell(new Phrase("Montant TTC", boldFont)));
        
        // Détails
        if (facture.getDetails() != null) {
            for (FactureDetail detail : facture.getDetails()) {
                table.addCell(new PdfPCell(new Phrase("Ligne " + detail.getNumeroLigneFact(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(detail.getMontantHT() != null ? detail.getMontantHT().toString() : "0", normalFont)));
                table.addCell(new PdfPCell(new Phrase(detail.getMontantTVA() != null ? detail.getMontantTVA().toString() : "0", normalFont)));
                table.addCell(new PdfPCell(new Phrase(detail.getMontantTTC() != null ? detail.getMontantTTC().toString() : "0", normalFont)));
            }
        }
        
        document.add(table);
        document.add(new Paragraph(" ")); // Espace
    }

    private void addTotal(Document document, FactureEntete facture) throws DocumentException {
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        
        // Calculer le total
        double totalHT = 0;
        double totalTVA = 0;
        double totalTTC = 0;
        
        if (facture.getDetails() != null) {
            for (FactureDetail detail : facture.getDetails()) {
                if (detail.getMontantHT() != null) totalHT += detail.getMontantHT().doubleValue();
                if (detail.getMontantTVA() != null) totalTVA += detail.getMontantTVA().doubleValue();
                if (detail.getMontantTTC() != null) totalTTC += detail.getMontantTTC().doubleValue();
            }
        }
        
        // Afficher les totaux
        document.add(new Paragraph("Total HT: " + String.format("%.2f", totalHT) + " MAD", boldFont));
        document.add(new Paragraph("Total TVA: " + String.format("%.2f", totalTVA) + " MAD", boldFont));
        document.add(new Paragraph("Total TTC: " + String.format("%.2f", totalTTC) + " MAD", boldFont));
    }
}
