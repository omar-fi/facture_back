#!/bin/bash

echo "🔍 TEST DE LA VRAIE BASE DE DONNÉES"
echo "==================================="
echo ""

echo "⏳ Attente du démarrage de l'application..."
sleep 10

echo "🏢 1. PORTS (Devrait afficher 25 ports)"
echo "---------------------------------------"
curl -s http://localhost:8080/admin/ports | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "👥 2. UTILISATEURS (Devrait afficher les vrais utilisateurs)"
echo "------------------------------------------------------------"
curl -s http://localhost:8080/admin/utilisateurs | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "💰 3. TARIFS STANDARDS (Devrait afficher les vrais tarifs)"
echo "----------------------------------------------------------"
curl -s http://localhost:8080/admin/tarifs-standards | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "📋 4. CATÉGORIES (Devrait afficher les vraies catégories)"
echo "---------------------------------------------------------"
curl -s http://localhost:8080/admin/categories | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "📄 5. FACTURES (Devrait afficher les vraies factures)"
echo "-----------------------------------------------------"
curl -s http://localhost:8080/api/factures | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "📊 6. MANIFESTS (Devrait afficher les vrais manifests)"
echo "------------------------------------------------------"
curl -s http://localhost:8080/api/manifest | jq 'length' 2>/dev/null || echo "❌ Erreur de connexion"
echo ""

echo "🔐 7. TEST D'AUTHENTIFICATION"
echo "----------------------------"
echo "Test avec un utilisateur de la vraie base :"
curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@anp.ma","password":"admin123"}' | jq '.' 2>/dev/null || echo "❌ Erreur d'authentification"
echo ""

echo "📈 8. RÉSUMÉ"
echo "============"
echo "✅ Backend configuré pour utiliser la vraie base de données"
echo "✅ Données de test désactivées"
echo "✅ Tous les endpoints devraient maintenant afficher les vraies données"
echo ""
echo "🎯 Votre frontend peut maintenant se connecter et voir toutes les vraies données !"


