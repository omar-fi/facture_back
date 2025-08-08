package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class InformationMarchandise {

    private String DesignationMarchandise;
    private String numeroConnaissement;
    private String CodeSH;
    private String Locode;
    private Double Tonnage;

    // getters/setters

    public String getDesignationMarchandise() {
        return DesignationMarchandise;
    }

    public void setDesignationMarchandise(String designationMarchandise) {
        DesignationMarchandise = designationMarchandise;
    }

    public String getNumeroConnaissement() {
        return numeroConnaissement;
    }

    public void setNumeroConnaissement(String numeroConnaissement) {
        this.numeroConnaissement = numeroConnaissement;
    }

    public Integer getCodeSH() {
        return CodeSH != null ? Integer.parseInt(CodeSH) : null;
    }

    public void setCodeSH(String codeSH) {
        CodeSH = codeSH;
    }

    public String getLocode() {
        return Locode;
    }

    public void setLocode(String locode) {
        Locode = locode;
    }

    public Double getTonnage() {
        return Tonnage;
    }

    public void setTonnage(Double tonnage) {
        Tonnage = tonnage;
    }
}
