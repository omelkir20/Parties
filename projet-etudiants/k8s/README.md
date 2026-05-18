# Déploiement Kubernetes — EduManager

## Prérequis

- kubectl configuré sur un cluster (K3S, Minikube, EKS, etc.)
- Images Docker publiées sur Docker Hub (remplacez `votre-username`)
- NGINX Ingress Controller installé

## Ordre de déploiement

```bash
# 1. Secrets (en premier, les autres en dépendent)
kubectl apply -f k8s/secrets/

# 2. Stockage persistant
kubectl apply -f k8s/postgres/postgres-pvc.yaml
kubectl apply -f k8s/mongodb/mongodb-pvc.yaml

# 3. Bases de données
kubectl apply -f k8s/postgres/postgres-deployment.yaml
kubectl apply -f k8s/mongodb/mongodb-pvc.yaml
kubectl apply -f k8s/redis/redis-deployment.yaml

# 4. Kafka
kubectl apply -f k8s/kafka/kafka-deployment.yaml

# 5. Micro services
kubectl apply -f k8s/etudiant-service/
kubectl apply -f k8s/grading-service/
kubectl apply -f k8s/notification-service/
kubectl apply -f k8s/auth-service/

# 6. Frontend
kubectl apply -f k8s/frontend/

# 7. Ingress
kubectl apply -f k8s/ingress.yaml
```

## Ou tout en une commande (ordre géré par dépendances)

```bash
kubectl apply -f k8s/ --recursive
```

## Vérification

```bash
kubectl get pods
kubectl get services
kubectl get ingress

# Logs d'un service
kubectl logs -l app=etudiant-service -f

# Accès local (ajouter dans /etc/hosts)
# 127.0.0.1 edumanager.local
```

## DNS interne Kubernetes

Chaque service est joignable depuis les autres pods via son nom :
- `http://etudiant-service:8081`
- `http://grading-service:8082`
- `http://kafka:9092`
- `http://postgres:5432`

Aucune configuration Eureka n'est nécessaire — Kubernetes gère la découverte nativement.
