-- Mettre à jour les manifests existants pour corriger les données manquantes

-- 1. Mettre à jour createdBy pour les manifests qui n'en ont pas
UPDATE manifeste SET created_by = 1 WHERE created_by IS NULL;

-- 2. Mettre à jour dateDepotManifest pour les manifests qui n'en ont pas
UPDATE manifeste SET date_depot_manifest = created_at WHERE date_depot_manifest IS NULL;

-- 3. Mettre à jour le statut pour les manifests qui n'en ont pas
UPDATE manifeste SET statut = 'EN_ATTENTE' WHERE statut IS NULL;

-- 4. Vérifier les données mises à jour
SELECT 
    id,
    statut,
    created_by,
    date_depot_manifest,
    trafic,
    port_id
FROM manifeste 
ORDER BY id;

