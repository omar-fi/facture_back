package org.example.stage_back.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    private final Long DEFAULT_PORT_ID = 1L; // <-- ID du port par défaut

    // Parse le fichier XML IFCSUM
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

        // 2) Contexte utilisateur (agent)
        final User user = userRepository.findById(agentId)
                .orElseThrow(() -> new IllegalStateException("Agent " + agentId + " introuvable"));

        // 3) Résolution du port
        final Long portId;
        String rawPort = header.getCodePortAnp();
        if (rawPort == null || rawPort.trim().isEmpty()) {
            portId = DEFAULT_PORT_ID;
            System.out.println("DEBUG: CodePortAnp manquant, port par défaut utilisé = " + portId);
        } else {
            String digits = rawPort.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) {
                portId = DEFAULT_PORT_ID;
                System.out.println("DEBUG: CodePortAnp non numérique, port par défaut utilisé = " + portId);
            } else {
                portId = Long.valueOf(digits);
            }
        }

        final Port port = portRepository.findById(portId).orElseGet(() -> {
            Port p = new Port();
            p.setId(portId);
            p.setNom("PORT-" + portId);
            p.setVille("INCONNU");
            p.setTauxRK(0.0);
            return portRepository.save(p);
        });

        // 4) Création du manifeste
        final Manifeste manifeste = new Manifeste();
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
            final Integer navireId = header.getNavireID();
            final Navire navire = navireRepository.findById(navireId).orElseGet(() -> {
                Navire n = new Navire();
                n.setNom("NAVIRE-" + navireId);
                return navireRepository.save(n);
            });
            manifeste.setNavire(navire);
        }

        // 6) Escale via NumeroAvis
        if (header.getNumeroAvis() != null && !header.getNumeroAvis().isEmpty()) {
            try {
                final Integer escaleId = Integer.valueOf(header.getNumeroAvis().trim());
                final Navire navireFinal = manifeste.getNavire(); // final pour le lambda
                final Escale escale = escaleRepository.findById(escaleId).orElseGet(() -> {
                    Escale e = new Escale();
                    e.setPort(port);
                    e.setNavire(navireFinal);
                    e.setDateArrivee(new Date());
                    e.setDateDepart(new Date());
                    return escaleRepository.save(e);
                });
                manifeste.setEscale(escale);
            } catch (NumberFormatException e) {
                System.out.println("WARN: NumeroAvis invalide pour l'escale : " + header.getNumeroAvis());
            }
        }

        // 7) Lignes
        DetailManifest detail = ifc.getDetailManifeste();
        if (detail != null && detail.getInformations() != null) {
            for (InformationMarchandise info : detail.getInformations()) {
                try {
                    final ManifestLine line = new ManifestLine();
                    line.setLibelleMH(info.getDesignationMarchandise());
                    line.setCodeSH(info.getCodeSH());
                    line.setPoids(info.getTonnage() != null ? info.getTonnage() : 0.0);
                    line.setVolume(0.0); // par défaut
                    line.setMarchandise(info.getDesignationMarchandise());
                    manifeste.addLine(line);
                } catch (Exception e) {
                    System.out.println("WARN: Ligne ignorée à cause d'une erreur: " + e.getMessage());
                }
            }
        }

        // 8) Sauvegarde
        final Manifeste saved = manifesteRepository.save(manifeste);
        System.out.println("DEBUG: manifeste id=" + saved.getId());
        return saved;
    }

}
