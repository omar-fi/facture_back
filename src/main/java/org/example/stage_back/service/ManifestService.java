package org.example.stage_back.service;

import org.example.stage_back.entities.*;
import org.example.stage_back.repository.*;
import org.example.stage_back.xml_parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ManifestService {

    private static final Logger logger = LoggerFactory.getLogger(ManifestService.class);

    @Autowired
    private ManifesteRepository manifesteRepository;

    @Autowired
    private ManifestLineRepository manifestLineRepository;

    @Autowired
    private UserRepo userRepository;

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
            logger.error("Erreur lors du parsing XML", e);
            throw new RuntimeException("Erreur lors du parsing XML", e);
        }
    }

    public void parseAndSaveManifest(MultipartFile file, String fichePath, Long agentId) {
        logger.info("=== DÉBUT UPLOAD MANIFEST ===");
        logger.info("Agent ID: {}", agentId);
        logger.info("Nom du fichier: {}", file.getOriginalFilename());
        logger.info("Taille du fichier: {} bytes", file.getSize());
        
        try {
            IfcSumXml ifcSumXml = parseXml(file);
            logger.info("XML parsé avec succès");

            Manifeste manifeste = new Manifeste();
            HeaderXml header = ifcSumXml.getHeader();
            logger.info("Header extrait: trafic={}", header.getTrafic());

            // Récupération des entités
            User user = userRepository.findById(agentId).orElse(null);
            Port port = portRepository.findById(1L).orElse(null);
            Escale escale = escaleRepository.findById(1).orElse(null);

            logger.info("Entités récupérées: user={}, port={}, escale={}", 
                user != null ? user.getId() : "null", 
                port != null ? port.getId() : "null", 
                escale != null ? escale.getId() : "null");

            manifeste.setUser(user);
            manifeste.setPort(port);
            manifeste.setEscale(escale);
            manifeste.setCreatedBy(agentId.intValue());

            // Parse date au format ISO
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateMsg = sdf.parse(header.getDateMessage());
                manifeste.setCreatedAt(dateMsg);
                manifeste.setDateDepotManifest(dateMsg);
                logger.info("Date parsée: {}", dateMsg);
            } catch (Exception e) {
                logger.warn("Erreur parsing date, utilisation date actuelle", e);
                manifeste.setCreatedAt(new Date());
                manifeste.setDateDepotManifest(new Date());
            }

            manifeste.setFichePath(fichePath);
            manifeste.setProcessedBy(null);
            manifeste.setTrafic(header.getTrafic());

            // Initialiser le statut par défaut
            manifeste.setStatut(Manifeste.StatutManifest.EN_ATTENTE);
            logger.info("Statut initialisé: {}", manifeste.getStatut());

            // Sauvegarde du manifeste
            Manifeste savedManifest = manifesteRepository.save(manifeste);
            logger.info("Manifest sauvegardé avec ID: {}", savedManifest.getId());

            // Parcours des lignes (marchandises)
            DetailManifest detail = ifcSumXml.getDetailManifeste();
            if (detail != null) {
                List<InformationMarchandise> infos = detail.getInformations();
                if (infos != null) {
                    logger.info("Traitement de {} lignes de marchandises", infos.size());
                    for (InformationMarchandise info : infos) {
                        ManifestLine line = new ManifestLine();

                        line.setLibelleMH(info.getDesignationMarchandise());
                        line.setCodeSH(info.getCodeSH());
                        line.setPoids(info.getTonnage() != null ? info.getTonnage() : 0.0);
                        line.setVolume(0.0);
                        line.setCategorie(null);
                        line.setMarchandise(info.getDesignationMarchandise());
                        line.setManifeste(manifeste);

                        ManifestLine savedLine = manifestLineRepository.save(line);
                        logger.info("Ligne sauvegardée: ID={}, poids={}, codeSH={}", 
                            savedLine.getId(), savedLine.getPoids(), savedLine.getCodeSH());
                    }
                } else {
                    logger.warn("Aucune information de marchandise trouvée");
                }
            } else {
                logger.warn("Aucun détail de manifest trouvé");
            }

            logger.info("=== FIN UPLOAD MANIFEST - SUCCÈS ===");
            
        } catch (Exception e) {
            logger.error("=== ERREUR LORS DE L'UPLOAD MANIFEST ===", e);
            throw e;
        }
    }
}
