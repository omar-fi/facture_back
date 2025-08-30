#!/bin/bash

echo "🧪 Test de l'API ANP"
echo "===================="

# Attendre que l'application démarre
echo "⏳ Attente du démarrage de l'application..."
sleep 15

# Test 1: Endpoint de base
echo "📡 Test 1: Endpoint de base"
curl -s "http://localhost:8080/api/test/hello" || echo "❌ Échec"

# Test 2: Compter les manifests
echo -e "\n📊 Test 2: Compter les manifests"
curl -s "http://localhost:8080/api/test/manifests/count" || echo "❌ Échec"

# Test 3: Compter les factures
echo -e "\n📄 Test 3: Compter les factures"
curl -s "http://localhost:8080/api/test/factures/count" || echo "❌ Échec"

# Test 4: Compter les agents
echo -e "\n👥 Test 4: Compter les agents"
curl -s "http://localhost:8080/api/test/agents/count" || echo "❌ Échec"

# Test 5: Statut complet
echo -e "\n🔍 Test 5: Statut complet"
curl -s "http://localhost:8080/api/test/status" | jq . 2>/dev/null || curl -s "http://localhost:8080/api/test/status"

# Test 6: Manifests en attente
echo -e "\n📋 Test 6: Manifests en attente"
curl -s "http://localhost:8080/api/taxateur/manifests/en-attente" | jq . 2>/dev/null || curl -s "http://localhost:8080/api/taxateur/manifests/en-attente"

# Test 7: Tous les manifests
echo -e "\n📋 Test 7: Tous les manifests"
curl -s "http://localhost:8080/api/manifest/all" | jq . 2>/dev/null || curl -s "http://localhost:8080/api/manifest/all"

echo -e "\n✅ Tests terminés"
