package org.example.stage_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stage_back.dto.ManifestDTO;
import org.example.stage_back.entities.*;
import org.example.stage_back.repository.*;
import org.example.stage_back.xml_parser.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ManifestService {

    private final ManifesteRepository manifesteRepository;
    private final ManifestLineRepository manifestLineRepository;
    private final UserRepo userRepository;
    private final PortRepository portRepository;
    private final EscaleRepository escaleRepository;
    private final NavireRepository navireRepository;

    /**
     * Parse le fichier XML IFCSUM en objets JAXB
     */
    private IfcSumXml parseXml(MultipartFile file) {
        try {
            JAXBContext context = JAXBContext.newInstance(IfcSumXml.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (IfcSumXml) unmarshaller.unmarshal(file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du parsing XML", e);
        }
    }

    @Transactional
    public Manifeste parseAndSaveManifest(MultipartFile file, String fichePath, Long agentId) {

        // 1) Parse XML
        IfcSumXml ifc = parseXml(file);
        if (ifc == null || ifc.getHeader() == null) {
            throw new IllegalArgumentException("XML IFCSUM invalide : <header> manquant.");
        }
        HeaderXml header = ifc.getHeader();

        // 2) Contexte utilisateur (agent qui upload)
        User user = userRepository.findById(agentId)
                .orElseThrow(() -> new IllegalStateException("Agent " + agentId + " introuvable"));

        // 3) Résolution du port via l'ID
        Long portId;
        try {
            portId = Long.valueOf(String.valueOf(header.getCodePortAnp()).trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("CodePortAnp invalide ou manquant dans le XML");
        }

        Port port = portRepository.findById(portId)
                .orElseThrow(() -> new IllegalStateException("Aucun port trouvé avec id=" + portId));

        // 4) Création du manifeste
        Manifeste manifeste = new Manifeste();
        manifeste.setUser(user);
        manifeste.setPort(port);
        manifeste.setCodeClient(header.getEmetteur());
        manifeste.setDateDepotManifest(new Date());
        manifeste.setFichePath(fichePath);
        manifeste.setCreatedBy(agentId);
        manifeste.setProcessedBy(null);
        manifeste.setTrafic(header.getTrafic());

        // 5) Navire
        if (header.getNavireID() != null) {
            navireRepository.findById(header.getNavireID()).ifPresent(manifeste::setNavire);
        }

        // 5bis) Escale via NumeroAvis
        if (header.getNumeroAvis() != null && !header.getNumeroAvis().isEmpty()) {
            try {
                Integer escaleId = Integer.valueOf(header.getNumeroAvis().trim());
                Escale escale = escaleRepository.findById(escaleId)
                        .orElseThrow(() -> new IllegalStateException("Aucune escale trouvée avec id=" + escaleId));
                manifeste.setEscale(escale);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("NumeroAvis invalide pour l'escale : " + header.getNumeroAvis());
            }
        }

        // 6) Lignes
        DetailManifest detail = ifc.getDetailManifeste();
        if (detail != null && detail.getInformations() != null) {
            for (InformationMarchandise info : detail.getInformations()) {
                ManifestLine line = new ManifestLine();
                line.setLibelleMH(info.getDesignationMarchandise());
                line.setCodeSH(info.getCodeSH());
                line.setPoids(info.getTonnage() != null ? info.getTonnage() : 0.0);
                line.setVolume(0.0);
                line.setMarchandise(info.getDesignationMarchandise());
                manifeste.addLine(line);
            }
        }

        // 7) Sauvegarde
        Manifeste saved = manifesteRepository.save(manifeste);
        System.out.println("DEBUG ManifestService: manifeste id=" + saved.getId());
        return saved;
    }

}
