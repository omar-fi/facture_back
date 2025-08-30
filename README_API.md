# API Documentation - Système de Traitement des Manifests et Facturation

## Vue d'ensemble

Ce système permet aux agents d'uploader des manifests et aux taxateurs de les traiter pour générer des factures détaillées au format ANP.

## Workflow

1. **Agent upload un manifest** → Manifest créé avec statut "EN_ATTENTE"
2. **Taxateur traite le manifest** → Manifest passe en statut "TRAITE" + Facture générée
3. **Agent peut télécharger la facture** → PDF au format ANP

## Endpoints

### 1. Upload de Manifest (Agent)

**POST** `/api/manifest/upload`
- **Description**: Upload d'un fichier manifest XML
- **Paramètres**: `file` (MultipartFile)
- **Réponse**: 200 OK si succès

### 2. Gestion des Manifests (Taxateur)

#### Lister les manifests en attente
**GET** `/api/taxateur/manifests/en-attente`
- **Description**: Liste tous les manifests en attente de traitement
- **Réponse**: Liste des manifests avec statut "EN_ATTENTE"

#### Lister les manifests traités
**GET** `/api/taxateur/manifests/traites?taxateurId={id}`
- **Description**: Liste tous les manifests traités par un taxateur
- **Paramètres**: `taxateurId` (Long)
- **Réponse**: Liste des manifests avec statut "TRAITE"

#### Récupérer un manifest
**GET** `/api/taxateur/manifests/{id}`
- **Description**: Récupère les détails d'un manifest
- **Paramètres**: `id` (Integer)
- **Réponse**: Détails du manifest

#### Traiter un manifest
**POST** `/api/taxateur/manifests/traiter?taxateurId={id}`
- **Description**: Traite un manifest et génère une facture
- **Paramètres**: `taxateurId` (Long)
- **Body**: `TraitementManifestRequest`
- **Réponse**: Facture générée

### 3. Gestion des Factures

#### Lister toutes les factures
**GET** `/api/factures`
- **Description**: Liste toutes les factures
- **Réponse**: Liste des factures

#### Récupérer une facture
**GET** `/api/factures/{id}`
- **Description**: Récupère les détails d'une facture
- **Paramètres**: `id` (Integer)
- **Réponse**: Détails de la facture

#### Télécharger une facture PDF
**GET** `/api/factures/{id}/download`
- **Description**: Télécharge une facture au format PDF
- **Paramètres**: `id` (Integer)
- **Réponse**: Fichier PDF

#### Lister les factures par escale
**GET** `/api/factures/escale/{escaleId}`
- **Description**: Liste les factures d'une escale spécifique
- **Paramètres**: `escaleId` (Long)
- **Réponse**: Liste des factures de l'escale

### 4. Gestion des Manifests (Agent)

#### Lister les manifests d'un agent
**GET** `/api/agent/manifests/{agentId}`
- **Description**: Liste tous les manifests créés par un agent
- **Paramètres**: `agentId` (Long)
- **Réponse**: Liste des manifests de l'agent

#### Lister les factures d'un agent
**GET** `/api/agent/factures/{agentId}`
- **Description**: Liste toutes les factures liées aux manifests d'un agent
- **Paramètres**: `agentId` (Long)
- **Réponse**: Liste des factures

#### Vérifier le statut d'un manifest
**GET** `/api/agent/manifests/{manifestId}/status`
- **Description**: Récupère le statut actuel d'un manifest
- **Paramètres**: `manifestId` (Integer)
- **Réponse**: Statut du manifest

## Modèles de Données

### TraitementManifestRequest
```json
{
  "manifestId": 1,
  "commentaires": "Traitement terminé avec succès",
  "lignes": [
    {
      "manifestLineId": 1,
      "tarifUnitaire": 10.50,
      "unite": "KG",
      "commentaire": "Marchandise standard"
    }
  ]
}
```

### Statuts des Manifests
- `EN_ATTENTE`: Manifest uploadé, en attente de traitement
- `EN_COURS`: En cours de traitement par le taxateur
- `TRAITE`: Traitement terminé, facture générée
- `ANNULE`: Manifest annulé

## Exemple d'utilisation

### 1. Agent upload un manifest
```bash
curl -X POST -F "file=@manifest.xml" http://localhost:8080/api/manifest/upload
```

### 2. Taxateur liste les manifests en attente
```bash
curl http://localhost:8080/api/taxateur/manifests/en-attente
```

### 3. Taxateur traite un manifest
```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"manifestId":1,"commentaires":"Traitement OK","lignes":[{"manifestLineId":1,"tarifUnitaire":15.0,"unite":"KG","commentaire":"Standard"}]}' \
  "http://localhost:8080/api/taxateur/manifests/traiter?taxateurId=1"
```

### 4. Agent télécharge sa facture
```bash
curl -O http://localhost:8080/api/factures/1/download
```

## Notes techniques

- Les factures sont générées au format PDF avec Apache PDFBox
- Le format suit le standard ANP (Agence Nationale des Ports)
- Les calculs incluent TVA 20% automatiquement
- Les manifests sont liés aux escales et ports
- Chaque ligne de manifest peut avoir un tarif personnalisé
