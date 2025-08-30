-- Script de configuration de la base de données pour Stage_back
-- Exécutez ce script dans votre base de données MySQL

-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS stage_back;
USE stage_back;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_type VARCHAR(50) DEFAULT 'AGENT'
);

-- Table des ports
CREATE TABLE IF NOT EXISTS port (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    ville VARCHAR(255),
    taux_rk DOUBLE
);

-- Table des escales
CREATE TABLE IF NOT EXISTS escale (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    port_id BIGINT,
    date_arrivee DATE,
    date_depart DATE,
    FOREIGN KEY (port_id) REFERENCES port(id)
);

-- Table des agents inscrits
CREATE TABLE IF NOT EXISTS agent_inscrit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    raison_sociale VARCHAR(255) NOT NULL,
    ice VARCHAR(15),
    password VARCHAR(255) NOT NULL,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    port_demande VARCHAR(255)
);

-- Table des agents
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    raison_sociale VARCHAR(255),
    port_demande VARCHAR(255),
    ice BIGINT
);

-- Table des manifests
CREATE TABLE IF NOT EXISTS manifeste (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    port_id BIGINT,
    escale_id BIGINT,
    date_depot_manifest DATE,
    fiche_path VARCHAR(500),
    created_by INT,
    processed_by INT,
    trafic VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'EN_ATTENTE',
    date_traitement TIMESTAMP NULL,
    commentaires_traitement TEXT,
    montant_total DOUBLE,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (port_id) REFERENCES port(id),
    FOREIGN KEY (escale_id) REFERENCES escale(id)
);

-- Table des lignes de manifest
CREATE TABLE IF NOT EXISTS manifest_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    manifeste_id INT,
    code_sh INT,
    categorie VARCHAR(100),
    marchandise VARCHAR(255),
    libelle_mh VARCHAR(255),
    poids DOUBLE,
    volume DOUBLE,
    FOREIGN KEY (manifeste_id) REFERENCES manifeste(id)
);

-- Table des factures
CREATE TABLE IF NOT EXISTS facture_entete (
    id INT AUTO_INCREMENT PRIMARY KEY,
    escale_id BIGINT,
    manifeste_id INT,
    date_emission_fact TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_reglement_fact TIMESTAMP NULL,
    date_annulation_fact TIMESTAMP NULL,
    FOREIGN KEY (escale_id) REFERENCES escale(id),
    FOREIGN KEY (manifeste_id) REFERENCES manifeste(id)
);

-- Table des détails de facture
CREATE TABLE IF NOT EXISTS facture_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    facture_entete_id INT,
    categorie_id INT,
    numero_ligne_fact INT,
    montant_ht DECIMAL(10,2),
    montant_tva DECIMAL(10,2),
    montant_tr DECIMAL(10,2),
    montant_ttc DECIMAL(10,2),
    FOREIGN KEY (facture_entete_id) REFERENCES facture_entete(id)
);

-- Insérer des données de test
INSERT IGNORE INTO user (id, email, password, role_type) VALUES 
(1, 'agent@anp.ma', 'password123', 'AGENT'),
(2, 'taxateur@anp.ma', 'password123', 'TAXATEUR');

INSERT IGNORE INTO port (id, nom, ville) VALUES 
(1, 'Port de Casablanca', 'Casablanca'),
(2, 'Port de Tanger', 'Tanger'),
(3, 'Port d\'Agadir', 'Agadir');

INSERT IGNORE INTO escale (id, port_id, date_arrivee, date_depart) VALUES 
(1, 1, '2024-01-15', '2024-01-20'),
(2, 2, '2024-01-16', '2024-01-21');

-- Vérifier que les tables sont créées
SHOW TABLES;
