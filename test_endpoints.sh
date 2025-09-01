#!/bin/bash

echo "=== Test des endpoints de l'application ==="

echo "1. Test de l'authentification admin"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "2. Test des ports (admin)"
curl -X GET http://localhost:8080/admin/ports \
  -w "\nStatus: %{http_code}\n\n"

echo "3. Test des utilisateurs (admin)"
curl -X GET http://localhost:8080/admin/utilisateurs \
  -w "\nStatus: %{http_code}\n\n"

echo "4. Test des manifests en attente (taxateur)"
curl -X GET http://localhost:8080/api/taxateur/manifests/en-attente \
  -w "\nStatus: %{http_code}\n\n"

echo "5. Test des manifests traités (taxateur)"
curl -X GET http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3 \
  -w "\nStatus: %{http_code}\n\n"

echo "6. Test des manifests de l'agent"
curl -X GET http://localhost:8080/api/agent/manifests/2 \
  -w "\nStatus: %{http_code}\n\n"

echo "7. Test des factures"
curl -X GET http://localhost:8080/api/factures \
  -w "\nStatus: %{http_code}\n\n"

echo "=== Fin des tests ==="
