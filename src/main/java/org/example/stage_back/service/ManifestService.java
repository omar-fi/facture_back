package org.example.stage_back.service;

import org.example.stage_back.entities.*;
import org.example.stage_back.repository.*;
import org.example.stage_back.xml_parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ManifestService {

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private ManifestLineRepository manifestLineRepository;

    @Autowired
    private UserRepo userRepository;  // corrigé

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private EscaleRepository escaleRepository;

    public IfcSumXml parseXml(MultipartFile file) {
        try {
            JAXBContext context = JAXBContext.newInstance(IfcSumXml.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (IfcSumXml) unmarshaller.unmarshal(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing XML", e);
        }
    }

    public void parseAndSaveManifest(MultipartFile file, String fichePath) {
        IfcSumXml ifcSumXml = parseXml(file);

        Manifeste manifeste = new Manifeste();

        HeaderXml header = ifcSumXml.getHeader();

        // Récupération des entités avec ID Long (ici 1L en exemple, à adapter)
        User user = userRepository.findById(1L).orElse(null);
        Port port = portRepository.findById(1L).orElse(null);
        Escale escale = escaleRepository.findById(1).orElse(null);

        manifeste.setUser(user);
        manifeste.setPort(port);
        manifeste.setEscale(escale);

        // Parse date au format ISO
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date dateMsg = sdf.parse(header.getDateMessage());
            manifeste.setCreatedAt(dateMsg);
            manifeste.setDateDepotManifest(dateMsg);
        } catch (Exception e) {
            manifeste.setCreatedAt(new Date());
            manifeste.setDateDepotManifest(new Date());
        }

        manifeste.setFichePath(fichePath);

        // Pas de processedBy dans ce XML, on met null
        manifeste.setProcessedBy(null);

        manifeste.setTrafic(header.getTrafic());

        // Sauvegarde du manifeste
        manifesteRepository.save(manifeste);

        // Parcours des lignes (marchandises)
        DetailManifest detail = ifcSumXml.getDetailManifeste();
        if (detail != null) {
            List<InformationMarchandise> infos = detail.getInformations();
            if (infos != null) {
                for (InformationMarchandise info : infos) {
                    ManifestLine line = new ManifestLine();

                    // Respecte la casse de tes attributs dans ManifestLine
                    line.setLibelleMH(info.getDesignationMarchandise());
                    line.setCodeSH(info.getCodeSH());
                    line.setPoids(info.getTonnage() != null ? info.getTonnage() : 0.0);
                    line.setVolume(0.0); // Pas présent dans le XML, à adapter
                    line.setCategorie(null); // Pas présent dans le XML, à adapter
                    line.setMarchandise(info.getDesignationMarchandise());
                    line.setManifeste(manifeste);

                    manifestLineRepository.save(line);
                }
            }
        }
    }
}
