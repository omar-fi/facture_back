package org.example.stage_back;

import org.example.stage_back.entities.Taxateur;
import org.example.stage_back.repository.TaxateurRepository;
import org.springframework.context.ApplicationContext;

import org.example.stage_back.entities.Admin;
import org.example.stage_back.entities.Agent;
import org.example.stage_back.entities.Port;
import org.example.stage_back.repository.AdminRepository;
import org.example.stage_back.repository.AgentRepository;
import org.example.stage_back.repository.PortRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;

@SpringBootApplication
public class StageBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(StageBackApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, AdminRepository adminRepository, PortRepository portRepository, AgentRepository agentRepository, TaxateurRepository taxateurRepository) {
        return args -> {
            Port port = new Port();
            port.setNom("Port Principal");
            port.setVille("Casablanca");
            port.setTauxRK(1.5);
            port = portRepository.save(port);

      
            Admin admin = new Admin();
            admin.setFullName("omar filali");
            admin.setEmail("admin@test.com");
            admin.setPassword("admin123");
            admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            adminRepository.save(admin);

            
            Agent agent = new Agent();
            agent.setFullName("Mohamed bounouar");
            agent.setEmail("agent@test.com");
            agent.setPassword("agent123");
            agent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            agent.setPort(port);
            agentRepository.save(agent);

            Taxateur taxateur = new Taxateur();
            taxateur.setFullName("Taxateur Test");
            taxateur.setEmail("taxateur@test.com");
            taxateur.setPassword("taxateur123");
            taxateur.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            taxateur.setPort(port);
            taxateurRepository.save(taxateur);
        };
    }
}
