package org.example.stage_back.xml_parser;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@XmlRootElement(name = "manifest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ManifestXml {

    @XmlElement(name = "header")
    private HeaderXml header;

    @XmlElementWrapper(name = "lines")
    @XmlElement(name = "line")
    private List<ManifestLineXml> lines;

    // **Le getter getHeader() doit être présent**
    public HeaderXml getHeader() {
        return header;
    }

    public void setHeader(HeaderXml header) {
        this.header = header;
    }

    public List<ManifestLineXml> getLines() {
        return lines;
    }

    public void setLines(List<ManifestLineXml> lines) {
        this.lines = lines;
    }
}
