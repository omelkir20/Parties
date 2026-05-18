# Projet Étudiants — API REST + Microservices + Mobile

Projet complet de gestion d'étudiants en architecture microservices, développé en trois parties.

---

## Structure du dépôt

```
/projet-etudiants/
├── api-spring-boot/       # Partie 1 & 2 — Micro service étudiant (port 8081)
├── grading-service/       # Partie 3 — Micro service notes (port 8082)
├── eureka-server/         # Partie 3 — Serveur de registre Eureka (port 8761)
├── api-gateway/           # Partie 3 — API Gateway Spring Cloud (port 8080)
├── frontend/              # Partie 3 — Application Next.js (port 3000)
├── mobile-app/            # Parties 1 & 3 — Application React Native (Expo)
├── k8s/                   # Partie 2 — Manifests Kubernetes
├── scripts/               # Scripts utilitaires (init base de données)
├── .github/               # Partie 3 — Templates GitHub et workflows CI
└── docker-compose.yml     # Partie 3 — Orchestration complète
```

---

## Partie 1 — API REST Spring Boot + Mobile

### Lancement rapide

```bash
# Démarrer la base de données + l'API
docker compose up postgres etudiant-service --build

# API accessible sur :
# http://localhost:8081/api/etudiants
# http://localhost:8081/swagger-ui.html
# http://localhost:8081/  (page HTML statique)
```

### Endpoints disponibles

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/etudiants | Liste tous les étudiants |
| GET | /api/etudiants?annee=2022 | Filtrer par année d'inscription |
| GET | /api/etudiants/{id} | Récupérer un étudiant |
| POST | /api/etudiants | Créer un étudiant |
| PUT | /api/etudiants/{id} | Mettre à jour un étudiant |
| DELETE | /api/etudiants/{id} | Supprimer un étudiant |
| GET | /api/departements | Liste tous les départements |
| GET | /api/departements/{id} | Récupérer un département |
| POST | /api/departements | Créer un département |
| PUT | /api/departements/{id} | Mettre à jour un département |
| DELETE | /api/departements/{id} | Supprimer un département |

### Application mobile (React Native / Expo)

```bash
cd mobile-app
npm install
npx expo start
```

> **Important :** Modifiez `src/api.ts` avec l'IP de votre machine (ex. `192.168.1.100`).

---

## Partie 2 — Enrichissements

### Tests BDD Cucumber

```bash
cd api-spring-boot
mvn test
```

Les scénarios Gherkin se trouvent dans :
`src/test/resources/features/etudiant.feature`

### Publication Docker Hub

```bash
cd api-spring-boot
mvn clean package -DskipTests
docker build -t votre-username/etudiant-service:1.0 .
docker push votre-username/etudiant-service:1.0
```

> Remplacez `votre-username` par votre identifiant Docker Hub.

### Déploiement Kubernetes (K3S)

```bash
# Mettre à jour l'image dans k8s/etudiant-deployment.yaml
kubectl apply -f k8s/

# Vérifier le déploiement
kubectl get pods
kubectl get services

# Accéder au service
kubectl port-forward svc/etudiant-service 8081:8081
# Ou via NodePort : http://<IP-VM>:30081/api/etudiants
```

---

## Partie 3 — Architecture Microservices complète

### Lancement complet

```bash
# S'assurer que Docker Desktop (ou Docker Engine) est démarré
docker compose up --build

# Les services démarrent dans cet ordre :
# 1. postgres (5432) + redis (6379)
# 2. eureka-server (8761)
# 3. etudiant-service (8081) + grading-service (8082)
# 4. api-gateway (8080)
# 5. frontend (3000)
```

### Services disponibles

| Service | URL | Description |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | Registre des services |
| API Gateway | http://localhost:8080 | Point d'entrée unique |
| Etudiant Service | http://localhost:8081/swagger-ui.html | Swagger étudiants |
| Grading Service | http://localhost:8082/swagger-ui.html | Swagger notes |
| Frontend Next.js | http://localhost:3000 | Interface web |

### API Gateway — Routes

Toutes les requêtes passent par `http://localhost:8080` :

```
GET  http://localhost:8080/api/etudiants       → etudiant-service
GET  http://localhost:8080/api/departements    → etudiant-service
GET  http://localhost:8080/api/notes           → grading-service
POST http://localhost:8080/api/notes           → grading-service
```

### Exemple — Créer une note

```bash
# Créer une note pour l'étudiant ID 1
curl -X POST http://localhost:8080/api/notes \
  -H "Content-Type: application/json" \
  -d '{"studentId": 1, "matiere": "Algorithmique", "valeur": 17.5}'
```

---

## Conventions de review (GitHub Workflow)

- Toute PR doit être relue dans les **48 heures** suivant son ouverture.
- Les commentaires bloquants doivent être **résolus avant le merge**.
- Chaque PR doit être liée à un **ticket Jira** (inclure la clé dans le titre et la description).
- Au moins **1 review approuvée** est requise avant de merger.
- Les pushs directs sur `main`, `version-2` et `version-3` sont **interdits**.
- Nommage des branches : `feature/PROJ-XX-description`, `fix/PROJ-XX-description`.
- Format des commits : `PROJ-XX : description courte en français`.

---

## Stack technique

| Couche | Technologie |
|--------|-------------|
| Backend | Spring Boot 3.3 + Java 21 |
| ORM | Spring Data JPA + Hibernate |
| Base de données | PostgreSQL 16 |
| Cache | Redis 7 |
| Découverte | Netflix Eureka |
| Communication | OpenFeign |
| Passerelle | Spring Cloud Gateway |
| Documentation | SpringDoc OpenAPI / Swagger |
| Tests | JUnit 5 + Cucumber (BDD) |
| Frontend | Next.js 14 + Tailwind CSS |
| Mobile | React Native (Expo) |
| Conteneurisation | Docker + Docker Compose |
| Orchestration | Kubernetes (K3S) |
| CI/CD | GitHub Actions |
