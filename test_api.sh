#!/bin/bash

echo "Test de l'API des manifests en attente..."
curl -X GET "http://localhost:8080/api/taxateur/manifests/en-attente" -H "Accept: application/json"

echo -e "\n\nTest de tous les manifests..."
curl -X GET "http://localhost:8080/api/taxateur/manifests/all" -H "Accept: application/json"

echo -e "\n\nTest de l'upload d'un manifest..."
curl -X POST "http://localhost:8080/api/manifest/upload" -F "file=@test.xml" -F "agentId=1"

echo -e "\n\nTest des manifests en attente après upload..."
curl -X GET "http://localhost:8080/api/taxateur/manifests/en-attente" -H "Accept: application/json"
