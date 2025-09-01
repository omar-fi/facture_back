#!/bin/bash

echo "=== Test de modification de mot de passe ==="

# ID de l'agent de test (agent@test.com)
AGENT_ID=2

echo "1. Test avec ancien mot de passe incorrect (devrait échouer)"
curl -X POST http://localhost:8080/api/agent/$AGENT_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "wrongpassword",
    "newPassword": "newpassword123",
    "confirmPassword": "newpassword123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "2. Test avec confirmation de mot de passe différente (devrait échouer)"
curl -X POST http://localhost:8080/api/agent/$AGENT_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "agent123",
    "newPassword": "newpassword123",
    "confirmPassword": "differentpassword"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "3. Test avec mot de passe trop court (devrait échouer)"
curl -X POST http://localhost:8080/api/agent/$AGENT_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "agent123",
    "newPassword": "123",
    "confirmPassword": "123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "4. Test de modification réussie (devrait réussir)"
curl -X POST http://localhost:8080/api/agent/$AGENT_ID/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "currentPassword": "agent123",
    "newPassword": "newagent123",
    "confirmPassword": "newagent123"
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "5. Test de connexion avec le nouveau mot de passe (devrait réussir)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"newagent123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "6. Test de connexion avec l'ancien mot de passe (devrait échouer)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"agent123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "7. Récupération du profil de l'agent"
curl -X GET http://localhost:8080/api/agent/$AGENT_ID/profile \
  -w "\nStatus: %{http_code}\n\n"
