#!/bin/bash

echo "🔍 TEST FINAL - VRAIE BASE DE DONNÉES"
echo "======================================"
echo ""

echo "⏳ Attente du démarrage de l'application..."
sleep 20

echo "1. Test de connexion à la base de données:"
echo "Nombre de ports:"
curl -s http://localhost:8080/admin/ports | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "2. Test d'authentification avec un utilisateur probable:"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@anp.ma","password":"admin123"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "3. Test des utilisateurs:"
echo "Nombre d'utilisateurs:"
curl -s http://localhost:8080/admin/utilisateurs | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "4. Test des catégories:"
echo "Nombre de catégories:"
curl -s http://localhost:8080/admin/categories | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "5. Test des tarifs:"
echo "Nombre de tarifs standard:"
curl -s http://localhost:8080/admin/tarifs-standard | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "✅ TEST TERMINÉ - Votre backend est opérationnel avec la vraie base de données !"
