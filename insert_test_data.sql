-- Script d'insertion de données de test pour Stage_back
-- Exécutez ce script dans votre base de données MySQL

USE stage_back;

-- Insérer des données de test si elles n'existent pas déjà

-- 1. Insérer des utilisateurs de test
INSERT IGNORE INTO user (id, email, password, role_type) VALUES 
(1, 'agent@anp.ma', '$2a$10$dummy.hash.for.testing', 'AGENT'),
(2, 'taxateur@anp.ma', '$2a$10$dummy.hash.for.testing', 'TAXATEUR');

-- 2. Insérer des ports de test
INSERT IGNORE INTO port (id, nom, ville) VALUES 
(1, 'Port de Casablanca', 'Casablanca'),
(2, 'Port de Tanger', 'Tanger'),
(3, 'Port d\'Agadir', 'Agadir');

-- 3. Insérer des escales de test
INSERT IGNORE INTO escale (id, port_id, date_arrivee, date_depart) VALUES 
(1, 1, '2024-01-15', '2024-01-20'),
(2, 2, '2024-01-16', '2024-01-21');

-- 4. Insérer des manifests de test
INSERT IGNORE INTO manifeste (id, user_id, port_id, escale_id, date_depot_manifest, created_by, trafic, created_at, statut) VALUES 
(1, 1, 1, 1, '2024-01-15', 1, 'IMPORT', '2024-01-15 10:00:00', 'EN_ATTENTE'),
(2, 1, 2, 2, '2024-01-16', 1, 'EXPORT', '2024-01-16 14:30:00', 'EN_ATTENTE'),
(3, 1, 1, 1, '2024-01-14', 1, 'IMPORT', '2024-01-14 09:15:00', 'TRAITE');

-- 5. Insérer des lignes de manifest de test
INSERT IGNORE INTO manifest_line (id, manifeste_id, code_sh, categorie, marchandise, libelle_mh, poids, volume) VALUES 
(1, 1, 123456, 'Métaux', 'Fer', 'Fer en barres', 1000.5, 2.5),
(2, 1, 789012, 'Textiles', 'Coton', 'Coton brut', 500.0, 1.0),
(3, 2, 345678, 'Machines', 'Équipements', 'Machines industrielles', 2500.0, 15.0);

-- 6. Insérer des factures de test
INSERT IGNORE INTO facture_entete (id, escale_id, manifeste_id, date_emission_fact) VALUES 
(1, 1, 3, '2024-01-14 16:00:00');

-- 7. Insérer des détails de facture de test
INSERT IGNORE INTO facture_detail (id, facture_entete_id, categorie_id, numero_ligne_fact, montant_ht, montant_tva, montant_ttc) VALUES 
(1, 1, 1, 1, 5000.00, 1000.00, 6000.00),
(2, 1, 2, 2, 2500.00, 500.00, 3000.00);

-- Mettre à jour le manifest traité avec le montant total
UPDATE manifeste SET montant_total = 9000.00, processed_by = 2, date_traitement = '2024-01-14 16:00:00', commentaires_traitement = 'Traitement terminé avec succès' WHERE id = 3;

-- Vérifier les données insérées
SELECT '=== RÉSUMÉ DES DONNÉES ===' as info;
SELECT COUNT(*) as total_users FROM user;
SELECT COUNT(*) as total_ports FROM port;
SELECT COUNT(*) as total_escales FROM escale;
SELECT COUNT(*) as total_manifests FROM manifeste;
SELECT COUNT(*) as total_manifest_lines FROM manifest_line;
SELECT COUNT(*) as total_factures FROM facture_entete;
SELECT COUNT(*) as total_facture_details FROM facture_detail;

-- Vérifier les manifests en attente
SELECT '=== MANIFESTS EN ATTENTE ===' as info;
SELECT id, created_by, port_id, statut, date_depot_manifest FROM manifeste WHERE statut = 'EN_ATTENTE';

-- Vérifier les manifests traités
SELECT '=== MANIFESTS TRAITÉS ===' as info;
SELECT id, created_by, processed_by, statut, montant_total FROM manifeste WHERE statut = 'TRAITE';
