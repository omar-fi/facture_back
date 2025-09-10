package org.example.stage_back.dto;

import java.util.ArrayList;
import java.util.List;

public class FactureDetailDTO {

    private double totalHT;
    private double totalTR;
    private double totalTVA;
    private double totalTTC;
    private List<FactureLigneDTO> lignes;

    public FactureDetailDTO() {}

    public FactureDetailDTO(double totalHT, double totalTR, double totalTVA, double totalTTC) {
        this.totalHT = totalHT;
        this.totalTR = totalTR;
        this.totalTVA = totalTVA;
        this.totalTTC = totalTTC;
        this.lignes = new ArrayList<>(); // ⚡ initialisation
    }

    public double getTotalHT() {
        return totalHT;
    }

    public void setTotalHT(double totalHT) {
        this.totalHT = totalHT;
    }

    public double getTotalTR() {
        return totalTR;
    }

    public void setTotalTR(double totalTR) {
        this.totalTR = totalTR;
    }

    public double getTotalTVA() {
        return totalTVA;
    }

    public void setTotalTVA(double totalTVA) {
        this.totalTVA = totalTVA;
    }

    public double getTotalTTC() {
        return totalTTC;
    }

    public void setTotalTTC(double totalTTC) {
        this.totalTTC = totalTTC;
    }
    public List<FactureLigneDTO> getLignes() {
        return lignes;
    }

    @Override
    public String toString() {
        return "FactureDetailDTO{" +
                "totalHT=" + totalHT +
                ", totalTR=" + totalTR +
                ", totalTVA=" + totalTVA +
                ", totalTTC=" + totalTTC +
                '}';
    }

    public void setLignes(List<FactureLigneDTO> factureLigneDTOS) {
        this.lignes = factureLigneDTOS;
    }

}
