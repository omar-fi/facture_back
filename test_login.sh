#!/bin/bash

echo "=== Test d'authentification ==="

echo "1. Test avec admin@test.com / admin123 (devrait réussir)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "2. Test avec agent@test.com / agent123 (devrait réussir)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"agent123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "3. Test avec taxateur@test.com / taxateur123 (devrait réussir)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"taxateur123"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "4. Test avec mauvais mot de passe (devrait échouer)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"wrongpassword"}' \
  -w "\nStatus: %{http_code}\n\n"

echo "5. Test avec utilisateur inexistant (devrait échouer)"
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"nonexistent@test.com","password":"password"}' \
  -w "\nStatus: %{http_code}\n\n"
