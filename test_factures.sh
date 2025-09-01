#!/bin/bash

echo "🧾 TEST DES FACTURES ET TÉLÉCHARGEMENT"
echo "========================================"
echo ""

echo "⏳ Attente du démarrage de l'application..."
sleep 20

echo "1. Test des factures de l'agent (ID: 1):"
echo "Nombre de factures:"
curl -s http://localhost:8080/api/agent/factures/1 | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "2. Détails des factures:"
curl -s http://localhost:8080/api/agent/factures/1 | jq '.[0:3] | .[] | {id: .id, dateEmission: .dateEmissionFact, portNom: .portNom}' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "3. Test des factures générales:"
echo "Nombre total de factures:"
curl -s http://localhost:8080/api/factures | jq 'length' 2>/dev/null || echo "❌ Erreur"
echo ""

echo "4. Test de téléchargement d'une facture (si elle existe):"
FACTURE_ID=$(curl -s http://localhost:8080/api/factures | jq '.[0].id' 2>/dev/null)
if [ "$FACTURE_ID" != "null" ] && [ "$FACTURE_ID" != "" ]; then
    echo "Tentative de téléchargement de la facture ID: $FACTURE_ID"
    curl -s -o "facture_test.pdf" http://localhost:8080/api/factures/$FACTURE_ID/download
    if [ -f "facture_test.pdf" ]; then
        echo "✅ PDF téléchargé avec succès (facture_test.pdf)"
        ls -la facture_test.pdf
    else
        echo "❌ Erreur lors du téléchargement"
    fi
else
    echo "❌ Aucune facture trouvée pour le test"
fi
echo ""

echo "5. Test de téléchargement via l'endpoint agent:"
if [ "$FACTURE_ID" != "null" ] && [ "$FACTURE_ID" != "" ]; then
    echo "Tentative de téléchargement via /api/agent/factures/$FACTURE_ID/download"
    curl -s -o "facture_agent_test.pdf" http://localhost:8080/api/agent/factures/$FACTURE_ID/download
    if [ -f "facture_agent_test.pdf" ]; then
        echo "✅ PDF téléchargé avec succès (facture_agent_test.pdf)"
        ls -la facture_agent_test.pdf
    else
        echo "❌ Erreur lors du téléchargement"
    fi
fi
echo ""

echo "✅ TEST TERMINÉ - Vérifiez les fichiers PDF générés !"


