package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ManifestLineXml {
    private String codesh;
    private Double poids;
    private Double volume;
    private String categorie;
    private String libellemh;
    private String marchandise;

}
