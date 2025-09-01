#!/bin/bash

echo "=== Test complet de tous les endpoints ==="
echo ""

echo "🔐 1. Test d'authentification"
echo "Admin:"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "Agent:"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"agent123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "Taxateur:"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"taxateur123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "🏢 2. Test des endpoints Admin"
echo "Ports:"
curl -X GET http://localhost:8080/admin/ports \
  -w "\nStatus: %{http_code}\n\n"

echo "Utilisateurs:"
curl -X GET http://localhost:8080/admin/utilisateurs \
  -w "\nStatus: %{http_code}\n\n"

echo "Tarifs standards:"
curl -X GET http://localhost:8080/admin/tarifs-standards \
  -w "\nStatus: %{http_code}\n\n"

echo "Tarifs spécifiques:"
curl -X GET http://localhost:8080/admin/tarifs-specifiques \
  -w "\nStatus: %{http_code}\n\n"

echo "Agents inscrits:"
curl -X GET http://localhost:8080/api/agent-inscrit/admin/agents-inscrits \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "📋 3. Test des endpoints Agent"
echo "Manifests de l'agent (ID=2):"
curl -X GET http://localhost:8080/api/agent/manifests/2 \
  -w "\nStatus: %{http_code}\n\n"

echo "Factures de l'agent (ID=2):"
curl -X GET http://localhost:8080/api/agent/factures/2 \
  -w "\nStatus: %{http_code}\n\n"

echo "Profil de l'agent (ID=2):"
curl -X GET http://localhost:8080/api/agent/2/profile \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "💰 4. Test des endpoints Taxateur"
echo "Manifests en attente:"
curl -X GET http://localhost:8080/api/taxateur/manifests/en-attente \
  -w "\nStatus: %{http_code}\n\n"

echo "Manifests traités (taxateur ID=3):"
curl -X GET "http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3" \
  -w "\nStatus: %{http_code}\n\n"

echo "Profil du taxateur (ID=3):"
curl -X GET http://localhost:8080/api/taxateur/3/profile \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "📄 5. Test des endpoints généraux"
echo "Toutes les factures:"
curl -X GET http://localhost:8080/api/factures \
  -w "\nStatus: %{http_code}\n\n"

echo "Tous les manifests:"
curl -X GET http://localhost:8080/api/manifest \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "✅ Test de modification de mot de passe (Taxateur)"
echo "Test avec ancien mot de passe incorrect:"
curl -X POST http://localhost:8080/api/taxateur/3/change-password \
  -H "Content-Type: application/json" \
  -d '{"currentPassword":"wrong","newPassword":"newpass123","confirmPassword":"newpass123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "Test avec confirmation différente:"
curl -X POST http://localhost:8080/api/taxateur/3/change-password \
  -H "Content-Type: application/json" \
  -d '{"currentPassword":"taxateur123","newPassword":"newpass123","confirmPassword":"different"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "Test de modification réussie:"
curl -X POST http://localhost:8080/api/taxateur/3/change-password \
  -H "Content-Type: application/json" \
  -d '{"currentPassword":"taxateur123","newPassword":"newpass123","confirmPassword":"newpass123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo ""
echo "=== Fin des tests ==="

