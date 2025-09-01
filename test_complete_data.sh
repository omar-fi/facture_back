#!/bin/bash

echo "🔍 TEST COMPLET DE TOUTES LES DONNÉES"
echo "====================================="
echo ""

echo "🏢 1. PORTS (Admin)"
echo "-------------------"
curl -s http://localhost:8080/admin/ports | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "👥 2. UTILISATEURS (Admin)"
echo "-------------------------"
curl -s http://localhost:8080/admin/utilisateurs | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "💰 3. TARIFS STANDARDS (Admin)"
echo "-----------------------------"
curl -s http://localhost:8080/admin/tarifs-standards | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "📋 4. MANIFESTS EN ATTENTE (Taxateur)"
echo "-----------------------------------"
curl -s http://localhost:8080/api/taxateur/manifests/en-attente | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "✅ 5. MANIFESTS TRAITÉS (Taxateur)"
echo "--------------------------------"
curl -s "http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3" | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "📄 6. FACTURES (Agent)"
echo "---------------------"
echo "Toutes les factures:"
curl -s http://localhost:8080/api/factures | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "Factures de l'agent (ID=2):"
curl -s http://localhost:8080/api/factures/agent/2 | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "📊 7. MANIFESTS DE L'AGENT (ID=2)"
echo "--------------------------------"
curl -s http://localhost:8080/api/agent/manifests/2 | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "👤 8. PROFILS"
echo "-------------"
echo "Profil de l'agent (ID=2):"
curl -s http://localhost:8080/api/agent/2/profile | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "Profil du taxateur (ID=3):"
curl -s http://localhost:8080/api/taxateur/3/profile | jq '.' 2>/dev/null || echo "Erreur ou pas de données"
echo ""

echo "🔐 9. TEST D'AUTHENTIFICATION"
echo "----------------------------"
echo "Admin:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification"
echo ""

echo "Agent:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"agent123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification"
echo ""

echo "Taxateur:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"taxateur123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification"
echo ""

echo "📈 10. RÉSUMÉ DES DONNÉES"
echo "========================="
echo "Nombre de ports: $(curl -s http://localhost:8080/admin/ports | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre d'utilisateurs: $(curl -s http://localhost:8080/admin/utilisateurs | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre de tarifs standards: $(curl -s http://localhost:8080/admin/tarifs-standards | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre de manifests en attente: $(curl -s http://localhost:8080/api/taxateur/manifests/en-attente | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre de manifests traités: $(curl -s 'http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3' | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre de factures: $(curl -s http://localhost:8080/api/factures | jq 'length' 2>/dev/null || echo '0')"
echo "Nombre de factures de l'agent: $(curl -s http://localhost:8080/api/factures/agent/2 | jq 'length' 2>/dev/null || echo '0')"

echo ""
echo "✅ TEST TERMINÉ"


