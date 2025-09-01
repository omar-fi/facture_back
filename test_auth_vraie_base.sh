#!/bin/bash

echo "🔐 TEST D'AUTHENTIFICATION - VRAIE BASE DE DONNÉES"
echo "=================================================="
echo ""

echo "⏳ Attente du démarrage de l'application..."
sleep 15

echo "1. Test avec admin@anp.ma (utilisateur probable de la vraie base):"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@anp.ma","password":"admin123"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "2. Test avec admin@test.com (utilisateur de test):"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"admin123"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "3. Test avec un email probable de la vraie base:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@port.ma","password":"admin123"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "4. Test avec un email générique:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin","password":"admin"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "5. Vérification des utilisateurs dans la base:"
echo "Nombre d'admins:"
curl -s http://localhost:8080/admin/utilisateurs | jq '.[] | select(.role=="ADMIN") | .email' 2>/dev/null || echo "❌ Erreur de récupération"
echo ""

echo "6. Test de connexion à la base de données:"
curl -s http://localhost:8080/admin/ports | jq 'length' 2>/dev/null && echo "✅ Connexion à la base OK" || echo "❌ Erreur de connexion à la base"
echo ""

echo "🔍 DIAGNOSTIC:"
echo "=============="
echo "Si tous les tests d'authentification échouent, cela peut être dû à :"
echo "1. Les mots de passe dans la vraie base ne sont pas encodés en BCrypt"
echo "2. Les emails ne correspondent pas à ceux de la vraie base"
echo "3. La structure des utilisateurs est différente"
echo ""
echo "SOLUTION: Vérifiez les vrais identifiants dans votre base de données !"


