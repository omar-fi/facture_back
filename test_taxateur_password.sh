#!/bin/bash

echo "=== Test de modification de mot de passe Taxateur ==="

# ID du taxateur de test (taxateur@test.com)
TAXATEUR_ID=3

echo "1. Test avec ancien mot de passe incorrect (devrait échouer)"
curl -X POST http://localhost:8080/api/taxateur/$TAXATEUR_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "wrongpassword",
    "newPassword": "newpassword123",
    "confirmPassword": "newpassword123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "2. Test avec confirmation de mot de passe différente (devrait échouer)"
curl -X POST http://localhost:8080/api/taxateur/$TAXATEUR_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "taxateur123",
    "newPassword": "newpassword123",
    "confirmPassword": "differentpassword"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "3. Test avec mot de passe trop court (devrait échouer)"
curl -X POST http://localhost:8080/api/taxateur/$TAXATEUR_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "taxateur123",
    "newPassword": "123",
    "confirmPassword": "123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "4. Test de modification réussie (devrait réussir)"
curl -X POST http://localhost:8080/api/taxateur/$TAXATEUR_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "taxateur123",
    "newPassword": "newtaxateur123",
    "confirmPassword": "newtaxateur123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "5. Test de connexion avec le nouveau mot de passe (devrait réussir)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"newtaxateur123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "6. Test de connexion avec l'ancien mot de passe (devrait échouer)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"taxateur123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "7. Récupération du profil du taxateur"
curl -X GET http://localhost:8080/api/taxateur/$TAXATEUR_ID/profile \
  -w "\nStatus: %{http_code}\n\n"
