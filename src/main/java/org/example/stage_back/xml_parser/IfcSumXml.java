package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "IFCSUM_XML")
@XmlAccessorType(XmlAccessType.FIELD)
public class IfcSumXml {

    @XmlElement(name = "header")
    private HeaderXml header;

    @XmlElement(name = "DetailManifeste")
    private DetailManifest detailManifeste;

    // getters/setters

    public HeaderXml getHeader() {
        return header;
    }

    public void setHeader(HeaderXml header) {
        this.header = header;
    }

    public DetailManifest getDetailManifeste() {
        return detailManifeste;
    }

    public void setDetailManifeste(DetailManifest detailManifeste) {
        this.detailManifeste = detailManifeste;
    }
}
