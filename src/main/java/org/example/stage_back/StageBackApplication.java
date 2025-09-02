package org.example.stage_back;

import org.example.stage_back.entities.*;
import org.example.stage_back.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.example.stage_back.service.DataSeedService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@SpringBootApplication
public class StageBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(StageBackApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx,
                                             AdminRepository adminRepository,
                                             AgentRepository agentRepository,
                                             TaxateurRepository taxateurRepository,
                                             PortRepository portRepository,
                                             ManifesteRepository manifesteRepository,
                                             NavireRepository navireRepository,
                                             TypeNavireRepository typeNavireRepository,
                                             EscaleRepository escaleRepository,
                                             ManifestLineRepository manifestLineRepository,
                                             FactureEnteteRepository factureEnteteRepository,
                                             FactureDetailRepository factureDetailRepository,
                                             CategoriesRepository categoriesRepository,
                                             TarifStandardRepository tarifStandardRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("Démarrage de l'application...");

            // Créer des catégories
            Categories categorie1 = new Categories();
            categorie1.setCategorie(1);
            categorie1.setLibelle("Marchandises générales");
            categorie1.setUnite(unite.Tonne);
            categorie1 = categoriesRepository.save(categorie1);

            Categories categorie2 = new Categories();
            categorie2.setCategorie(2);
            categorie2.setLibelle("Conteneurs");
            categorie2.setUnite(unite.Unité);
            categorie2 = categoriesRepository.save(categorie2);

            // Créer un port
            Port port = new Port();
            port.setNom("Port Principal");
            port.setVille("Casablanca");
            port.setTauxRK(10.0);
            port = portRepository.save(port);

            // Créer un admin
            Admin admin = new Admin();
            admin.setEmail("admin@test.com");
            admin.setPassword("admin123");
            admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            admin = adminRepository.save(admin);

            // Associer l'admin au port
            port.setAdmin(admin);
            portRepository.save(port);



            Agent agent1 = new Agent();
            agent1.setEmail("agent1@test.com");
            agent1.setPassword("agent123");
            agent1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            agent1.setRaisonSociale("Entreprise Test Agent");
            agent1.setPortDemande("Port Principal casa");
            agent1.setPort(port);
            agentRepository.save(agent1);
            // Créer un agent
            Agent agent = new Agent();
            agent.setEmail("agent@test.com");
            agent.setPassword("agent123");
            agent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            agent.setRaisonSociale("Entreprise Test Agent");
            agent.setPortDemande("Port Principal");
            agent.setPort(port);
            agentRepository.save(agent);

            // Créer un taxateur
            Taxateur taxateur = new Taxateur();
            taxateur.setEmail("taxateur@test.com");
            taxateur.setPassword("taxateur123");
            taxateur.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            taxateur.setPort(port);
            taxateur = taxateurRepository.save(taxateur);

            // Créer des tarifs standards
            TarifStandard tarif1 = new TarifStandard();
            tarif1.setPortId(port.getId().intValue());
            tarif1.setCategorie("Marchandises");
            tarif1.setLibelle("Droit de quai");
            tarif1.setUnite(unite.Tonne);
            tarif1.setTarifUnitaire(100.0);
            tarif1.setGroupName("Droits portuaires");
            tarifStandardRepository.save(tarif1);

            // Créer des types de navires
            TypeNavire typeNavire = new TypeNavire();
            typeNavire.setLibelle("Cargo");
            typeNavire = typeNavireRepository.save(typeNavire);

            // Créer un navire
            Navire navire = new Navire();
            navire.setTypeNavire(typeNavire);
            navire = navireRepository.save(navire);

            // Créer une escale
            Escale escale = new Escale();
            escale.setNavire(navire);
            escale.setPort(port);
            escale.setDateArrivee(new Date());
            escale.setDateDepart(new Date());
            escale = escaleRepository.save(escale);

            // Créer des manifests
            Manifeste manifest1 = new Manifeste();
            manifest1.setTrafic("IMPORT");
            manifest1.setDateDepotManifest(new Date());
            manifest1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            manifest1.setCreatedBy(agent.getId());
            manifest1.setStatut(Manifeste.StatutManifest.EN_ATTENTE);
            manifest1.setPort(port);
            manifest1.setUser(agent);
            manifest1.setEscale(escale);
            manifest1 = manifesteRepository.save(manifest1);

            Manifeste manifest2 = new Manifeste();
            manifest2.setTrafic("IMPORT");
            manifest2.setDateDepotManifest(new Date());
            manifest2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            manifest2.setCreatedBy(agent.getId());
            manifest2.setProcessedBy(taxateur.getId());
            manifest2.setStatut(Manifeste.StatutManifest.TRAITE);
            manifest2.setDateTraitement(new Date());
            manifest2.setCommentairesTraitement("Manifest traité avec succès");
            manifest2.setPort(port);
            manifest2.setUser(agent);
            manifest2.setEscale(escale);
            manifest2 = manifesteRepository.save(manifest2);

            Manifeste manifest3 = new Manifeste();
            manifest3.setTrafic("IMPORT");
            manifest3.setDateDepotManifest(new Date());
            manifest3.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            manifest3.setCreatedBy(agent.getId());
            manifest3.setStatut(Manifeste.StatutManifest.TRAITE);
            manifest3.setPort(port);
            manifest3.setUser(agent);
            manifest3.setEscale(escale);
            manifest3 = manifesteRepository.save(manifest3);

            // Créer des lignes de manifest
            ManifestLine ligne1 = new ManifestLine();
            ligne1.setManifeste(manifest1);
            ligne1.setPoids(100.0);
            manifestLineRepository.save(ligne1);

            ManifestLine ligne2 = new ManifestLine();
            ligne2.setManifeste(manifest1);
            ligne2.setPoids(200.0);
            manifestLineRepository.save(ligne2);

            ManifestLine ligne3 = new ManifestLine();
            ligne3.setManifeste(manifest2);
            ligne3.setPoids(100.0);
            manifestLineRepository.save(ligne3);

            ManifestLine ligne4 = new ManifestLine();
            ligne4.setManifeste(manifest2);
            ligne4.setPoids(200.0);
            manifestLineRepository.save(ligne4);

            ManifestLine ligne5 = new ManifestLine();
            ligne5.setManifeste(manifest3);
            ligne5.setPoids(100.0);
            manifestLineRepository.save(ligne5);

            ManifestLine ligne6 = new ManifestLine();
            ligne6.setManifeste(manifest3);
            ligne6.setPoids(200.0);
            manifestLineRepository.save(ligne6);

            // Créer des factures
            FactureEntete facture1 = new FactureEntete();
            facture1.setDateEmissionFact(new Date());
            facture1.setManifeste(manifest2);
            facture1.setEscale(escale);
            facture1 = factureEnteteRepository.save(facture1);

            FactureEntete facture2 = new FactureEntete();
            facture2.setDateEmissionFact(new Date());
            facture2.setManifeste(manifest3);
            facture2.setEscale(escale);
            facture2 = factureEnteteRepository.save(facture2);

            // Créer des détails de facture
            FactureDetail detail1 = new FactureDetail();
            detail1.setFactureEntete(facture1);
            detail1.setCategorieId(categorie1.getId());
            
            detail1.setMontantHT(new BigDecimal("1000.00"));
            detail1.setMontantTVA(new BigDecimal("200.00"));
            detail1.setMontantTR(new BigDecimal("50.00"));
            detail1.setMontantTTC(new BigDecimal("1250.00"));
            factureDetailRepository.save(detail1);

            FactureDetail detail2 = new FactureDetail();
            detail2.setFactureEntete(facture2);
            detail2.setCategorieId(categorie2.getId());
            
            detail2.setMontantHT(new BigDecimal("500.00"));
            detail2.setMontantTVA(new BigDecimal("100.00"));
            detail2.setMontantTR(new BigDecimal("25.00"));
            detail2.setMontantTTC(new BigDecimal("625.00"));
            factureDetailRepository.save(detail2);

            System.out.println("Données de test créées avec succès !");
        };
    }

    @Bean
    public CommandLineRunner seedRunner(DataSeedService dataSeedService) {
        return args -> {
            try {
                dataSeedService.insertSampleData();
                System.out.println("Données seed insérées via CommandLineRunner.");
            } catch (Exception ex) {
                System.out.println("Insertion seed ignorée: " + ex.getMessage());
            }
        };
    }

}
