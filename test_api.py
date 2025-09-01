#!/usr/bin/env python3
import requests
import json

def test_api():
    base_url = "http://localhost:8080"
    
    print("🧪 Test de l'API des manifests")
    print("=" * 40)
    
    # Test 1: Manifests en attente
    print("\n1. Test des manifests en attente...")
    try:
        response = requests.get(f"{base_url}/api/taxateur/manifests/en-attente")
        print(f"Status: {response.status_code}")
        if response.status_code == 200:
            data = response.json()
            print(f"Manifests en attente: {len(data)}")
            if data:
                print("Premier manifest:")
                print(json.dumps(data[0], indent=2, default=str))
        else:
            print(f"Erreur: {response.text}")
    except Exception as e:
        print(f"Erreur: {e}")
    
    # Test 2: Tous les manifests
    print("\n2. Test de tous les manifests...")
    try:
        response = requests.get(f"{base_url}/api/taxateur/manifests/all")
        print(f"Status: {response.status_code}")
        if response.status_code == 200:
            data = response.json()
            print(f"Total manifests: {len(data)}")
            if data:
                print("Premier manifest:")
                print(json.dumps(data[0], indent=2, default=str))
        else:
            print(f"Erreur: {response.text}")
    except Exception as e:
        print(f"Erreur: {e}")
    
    # Test 3: Upload d'un manifest
    print("\n3. Test de l'upload d'un manifest...")
    try:
        with open('test.xml', 'rb') as f:
            files = {'file': f}
            data = {'agentId': '1'}
            response = requests.post(f"{base_url}/api/manifest/upload", files=files, data=data)
            print(f"Status: {response.status_code}")
            if response.status_code == 201:
                print("✅ Upload réussi!")
            else:
                print(f"Erreur: {response.text}")
    except Exception as e:
        print(f"Erreur: {e}")
    
    # Test 4: Manifests en attente après upload
    print("\n4. Test des manifests en attente après upload...")
    try:
        response = requests.get(f"{base_url}/api/taxateur/manifests/en-attente")
        print(f"Status: {response.status_code}")
        if response.status_code == 200:
            data = response.json()
            print(f"Manifests en attente: {len(data)}")
            if data:
                print("Premier manifest:")
                print(json.dumps(data[0], indent=2, default=str))
        else:
            print(f"Erreur: {response.text}")
    except Exception as e:
        print(f"Erreur: {e}")

if __name__ == "__main__":
    test_api()

