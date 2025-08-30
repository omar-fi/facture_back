#!/bin/bash

echo "🔄 Redémarrage du backend ANP"
echo "=============================="

# 1. Arrêter tous les processus Java
echo "⏹️  Arrêt des processus Java..."
pkill -f "spring-boot:run" 2>/dev/null
pkill -f "java.*StageBackApplication" 2>/dev/null
sleep 3

# 2. Vérifier qu'aucun processus n'utilise le port 8080
echo "🔍 Vérification du port 8080..."
if lsof -ti:8080 > /dev/null 2>&1; then
    echo "⚠️  Port 8080 encore utilisé, arrêt forcé..."
    lsof -ti:8080 | xargs kill -9 2>/dev/null
    sleep 2
fi

# 3. Nettoyer la base de données (optionnel)
echo "🗑️  Nettoyage de la base de données..."
mysql -u root -e "DROP DATABASE IF EXISTS stage_back; CREATE DATABASE stage_back;" 2>/dev/null || echo "⚠️  Impossible de nettoyer la base de données"

# 4. Compiler le projet
echo "🔨 Compilation du projet..."
./mvnw -q -DskipTests clean compile

# 5. Démarrer l'application
echo "🚀 Démarrage de l'application..."
./mvnw spring-boot:run
