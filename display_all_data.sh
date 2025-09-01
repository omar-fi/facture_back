#!/bin/bash

echo "🗄️  AFFICHAGE COMPLET DE TOUTES LES DONNÉES DE LA BASE"
echo "=================================================="
echo ""

echo "🔐 UTILISATEURS"
echo "---------------"
echo "Admin:"
curl -s http://localhost:8080/admin/utilisateurs | jq '.[] | select(.role=="ADMIN")' 2>/dev/null || echo "Aucun admin trouvé"
echo ""

echo "Agents:"
curl -s http://localhost:8080/admin/utilisateurs | jq '.[] | select(.role=="AGENT")' 2>/dev/null || echo "Aucun agent trouvé"
echo ""

echo "Taxateurs:"
curl -s http://localhost:8080/admin/utilisateurs | jq '.[] | select(.role=="TAXATEUR")' 2>/dev/null || echo "Aucun taxateur trouvé"
echo ""

echo "🏢 PORTS"
echo "--------"
curl -s http://localhost:8080/admin/ports | jq '.' 2>/dev/null || echo "Aucun port trouvé"
echo ""

echo "📋 MANIFESTS"
echo "------------"
echo "Manifests en attente:"
curl -s http://localhost:8080/api/taxateur/manifests/en-attente | jq '.' 2>/dev/null || echo "Aucun manifest en attente"
echo ""

echo "Manifests traités:"
curl -s "http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3" | jq '.' 2>/dev/null || echo "Aucun manifest traité"
echo ""

echo "Manifests de l'agent (ID=2):"
curl -s http://localhost:8080/api/agent/manifests/2 | jq '.' 2>/dev/null || echo "Aucun manifest pour cet agent"
echo ""

echo "📄 FACTURES"
echo "-----------"
curl -s http://localhost:8080/api/factures | jq '.' 2>/dev/null || echo "Aucune facture trouvée"
echo ""

echo "💰 TARIFS"
echo "---------"
echo "Tarifs standards:"
curl -s http://localhost:8080/admin/tarifs-standards | jq '.' 2>/dev/null || echo "Aucun tarif standard"
echo ""

echo "Tarifs spécifiques:"
curl -s http://localhost:8080/admin/tarifs-specifiques | jq '.' 2>/dev/null || echo "Aucun tarif spécifique"
echo ""

echo "👥 PROFILS"
echo "----------"
echo "Profil de l'agent (ID=2):"
curl -s http://localhost:8080/api/agent/2/profile | jq '.' 2>/dev/null || echo "Profil non trouvé"
echo ""

echo "Profil du taxateur (ID=3):"
curl -s http://localhost:8080/api/taxateur/3/profile | jq '.' 2>/dev/null || echo "Profil non trouvé"
echo ""

echo "📊 RÉSUMÉ DES DONNÉES"
echo "===================="
echo ""

# Compter les utilisateurs
echo "Nombre d'utilisateurs par rôle:"
curl -s http://localhost:8080/admin/utilisateurs | jq -r '. | group_by(.role) | map({role: .[0].role, count: length}) | .[]' 2>/dev/null || echo "Impossible de compter les utilisateurs"
echo ""

# Compter les manifests
echo "Nombre de manifests par statut:"
echo "En attente: $(curl -s http://localhost:8080/api/taxateur/manifests/en-attente | jq 'length' 2>/dev/null || echo '0')"
echo "Traités: $(curl -s 'http://localhost:8080/api/taxateur/manifests/traites?taxateurId=3' | jq 'length' 2>/dev/null || echo '0')"
echo ""

echo "✅ AFFICHAGE TERMINÉ"
