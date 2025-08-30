# API de Gestion des Manifests et Factures - ANP

## 🎯 **Vue d'ensemble**

Cette API permet aux **agents** d'uploader des manifests et aux **taxateurs** de les traiter pour générer des factures PDF au format ANP.

## 🔄 **Workflow complet**

1. **Agent** → Upload un manifest XML
2. **Taxateur** → Traite le manifest et génère une facture
3. **Agent** → Reçoit la facture et peut la télécharger en PDF
4. **Agent** → Peut consulter l'historique de ses manifests et factures

## 📋 **Endpoints disponibles**

### **Manifests**
- `POST /api/manifest/upload` - Upload d'un manifest par un agent
- `GET /api/manifest/all` - Liste tous les manifests (debug)
- `GET /api/agent/manifests/{agentId}` - Manifests d'un agent spécifique

### **Traitement par le Taxateur**
- `GET /api/taxateur/manifests/en-attente` - Manifests en attente de traitement
- `GET /api/taxateur/manifests/traites?taxateurId=X` - Manifests traités par un taxateur
- `POST /api/taxateur/manifests/traiter` - Traite un manifest et génère une facture

### **Factures**
- `GET /api/factures/` - Liste toutes les factures
- `GET /api/factures/{id}` - Détails d'une facture
- `GET /api/factures/{id}/download` - Télécharge une facture en PDF
- `GET /api/factures/agent/{agentId}` - Factures d'un agent spécifique
- `GET /api/agent/factures/{agentId}` - Factures d'un agent (via contrôleur agent)

### **Agents**
- `POST /api/agent-inscrit/register` - Inscription d'un nouvel agent
- `GET /api/agent-inscrit/admin/agents-inscrits` - Liste des agents en attente
- `POST /api/agent-inscrit/admin/agents-inscrits/{id}/accepter` - Accepter un agent

### **Test et Debug**
- `GET /api/test/hello` - Test de l'API
- `GET /api/test/manifests/count` - Nombre total de manifests
- `GET /api/test/factures/count` - Nombre total de factures
- `GET /api/test/agents/count` - Nombre total d'agents inscrits
- `GET /api/test/status` - Statut complet de l'API

## 🗄️ **Configuration de la base de données**

### **1. Créer la base de données**
```bash
mysql -u root -p < database_setup.sql
```

### **2. Vérifier la configuration**
```properties
# Dans application.properties
spring.jpa.hibernate.ddl-auto=none
spring.datasource.url=jdbc:mysql://localhost:3306/stage_back
```

## 📊 **Format des données**

### **Upload de manifest**
```bash
curl -X POST "http://localhost:8080/api/manifest/upload" \
  -F "file=@manifest.xml" \
  -F "agentId=1"
```

### **Traitement d'un manifest**
```bash
curl -X POST "http://localhost:8080/api/taxateur/manifests/traiter?taxateurId=1" \
  -H "Content-Type: application/json" \
  -d '{
    "manifestId": 1,
    "commentaires": "Traitement terminé",
    "lignes": [
      {
        "manifestLineId": 1,
        "tarifUnitaire": 100.0,
        "unite": "KG",
        "commentaire": "Marchandise standard"
      }
    ]
  }'
```

### **Téléchargement d'une facture**
```bash
curl -X GET "http://localhost:8080/api/factures/1/download" \
  -H "Accept: application/pdf" \
  --output "facture_1.pdf"
```

## 🎨 **Format de la facture PDF**

La facture générée inclut :
- **En-tête ANP** avec logo et informations
- **Numéro de facture** unique
- **Informations client** (agent)
- **Détails des marchandises** du manifest
- **Calculs automatiques** (HT, TVA, TTC)
- **Pied de page** avec informations de contact

## 🔧 **Configuration requise**

### **Dépendances Maven**
```xml
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>2.0.29</version>
</dependency>
```

### **Base de données**
- MySQL avec les tables : `manifeste`, `facture_entete`, `facture_detail`, `user`, `port`, `escale`, `agent_inscrit`, `agent`

## 🚀 **Démarrage rapide**

1. **Configurer la base de données**
   ```bash
   mysql -u root -p < database_setup.sql
   ```

2. **Compiler le projet**
   ```bash
   ./mvnw clean compile
   ```

3. **Démarrer l'application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Tester l'API**
   ```bash
   curl http://localhost:8080/api/test/hello
   ```

## 📝 **Exemples d'utilisation**

### **1. Inscription d'un agent**
```bash
curl -X POST "http://localhost:8080/api/agent-inscrit/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nouveau@agent.ma",
    "raisonSociale": "Nouvelle Société",
    "ice": "123456789012345",
    "password": "password123"
  }'
```

### **2. Agent upload un manifest**
```bash
# L'agent upload son manifest
curl -X POST "http://localhost:8080/api/manifest/upload" \
  -F "file=@manifest.xml" \
  -F "agentId=1"
```

### **3. Taxateur traite le manifest**
```bash
# Le taxateur voit les manifests en attente
curl "http://localhost:8080/api/taxateur/manifests/en-attente"

# Le taxateur traite un manifest
curl -X POST "http://localhost:8080/api/taxateur/manifests/traiter?taxateurId=1" \
  -H "Content-Type: application/json" \
  -d '{"manifestId": 1, "commentaires": "OK", "lignes": []}'
```

### **4. Agent télécharge sa facture**
```bash
# L'agent voit ses factures
curl "http://localhost:8080/api/agent/factures/1"

# L'agent télécharge sa facture en PDF
curl "http://localhost:8080/api/factures/1/download" --output "ma_facture.pdf"
```

## 🔍 **Débogage**

### **Vérifier l'état de l'API**
```bash
# Statut complet
curl "http://localhost:8080/api/test/status"

# Compter les manifests
curl "http://localhost:8080/api/test/manifests/count"

# Voir tous les manifests
curl "http://localhost:8080/api/manifest/all"

# Voir les manifests en attente
curl "http://localhost:8080/api/taxateur/manifests/en-attente"
```

### **Logs de l'application**
L'application génère des logs détaillés pour :
- Upload de manifests
- Traitement par les taxateurs
- Génération de factures
- Envoi de notifications
- Inscription d'agents

## 📞 **Support**

Pour toute question ou problème :
1. Vérifiez les logs de l'application
2. Testez les endpoints de debug
3. Vérifiez la base de données avec `database_setup.sql`
4. Consultez la documentation des entités JPA
