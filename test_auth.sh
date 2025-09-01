#!/bin/bash

echo "🔐 TEST D'AUTHENTIFICATION"
echo "=========================="
echo ""

echo "1. Test Admin:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification admin"
echo ""

echo "2. Test Agent:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"agent@test.com","password":"agent123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification agent"
echo ""

echo "3. Test Taxateur:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"taxateur@test.com","password":"taxateur123"}' | jq '.' 2>/dev/null || echo "Erreur d'authentification taxateur"
echo ""

echo "4. Test avec mauvais mot de passe:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"wrongpassword"}' | jq '.' 2>/dev/null || echo "Erreur attendue avec mauvais mot de passe"
echo ""

echo "✅ Test terminé"


