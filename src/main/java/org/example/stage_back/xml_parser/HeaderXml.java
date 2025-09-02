package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class HeaderXml {
    private String Emetteur;
    private String Destinataire;
    private String DateMessage;
    private String TypeMessage;
    private String FonctionMessage;
    private String NumeroMessage;
    private String NumeroAvis;
    private String CodeEdiOperateur;
    private String codeEdiConsignataire;
    private String CodePortAnp;
    private String Trafic;
    private String NavireID;

    // getters/setters

    public String getEmetteur() { return Emetteur; }
    public void setEmetteur(String emetteur) { this.Emetteur = emetteur; }

    public String getDestinataire() { return Destinataire; }
    public void setDestinataire(String destinataire) { this.Destinataire = destinataire; }

    public String getDateMessage() { return DateMessage; }
    public void setDateMessage(String dateMessage) { this.DateMessage = dateMessage; }

    public String getTypeMessage() { return TypeMessage; }
    public void setTypeMessage(String typeMessage) { this.TypeMessage = typeMessage; }

    public String getFonctionMessage() { return FonctionMessage; }
    public void setFonctionMessage(String fonctionMessage) { this.FonctionMessage = fonctionMessage; }

    public String getNumeroMessage() { return NumeroMessage; }
    public void setNumeroMessage(String numeroMessage) { this.NumeroMessage = numeroMessage; }

    public String getNumeroAvis() { return NumeroAvis; }
    public void setNumeroAvis(String numeroAvis) { this.NumeroAvis = numeroAvis; }

    public String getCodeEdiOperateur() { return CodeEdiOperateur; }
    public void setCodeEdiOperateur(String codeEdiOperateur) { this.CodeEdiOperateur = codeEdiOperateur; }

    public String getCodeEdiConsignataire() { return codeEdiConsignataire; }
    public void setCodeEdiConsignataire(String codeEdiConsignataire) { this.codeEdiConsignataire = codeEdiConsignataire; }

    public String getCodePortAnp() { return CodePortAnp; }
    public void setCodePortAnp(String codePortAnp) { this.CodePortAnp = codePortAnp; }

    public String getTrafic() { return Trafic; }
    public void setTrafic(String trafic) { this.Trafic = trafic; }

    public Integer getNavireID() {
        if (NavireID == null || NavireID.trim().isEmpty()) {
            return null; // pas de navire dans ce manifeste
        }
        try {
            return Integer.parseInt(NavireID.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("NavireID invalide : " + NavireID, e);
        }
    }

}