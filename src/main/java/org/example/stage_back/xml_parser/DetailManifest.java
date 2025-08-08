package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DetailManifest {

    @XmlElement(name = "InformationMarchandise")
    private List<InformationMarchandise> informations;

    public List<InformationMarchandise> getInformations() {
        return informations;
    }

    public void setInformations(List<InformationMarchandise> informations) {
        this.informations = informations;
    }
}
