🛠️ Pipeline CI/CD pour l'architecture microservices (branche backend)
Ce projet utilise GitHub Actions pour automatiser l'intégration et le déploiement continu.

📂 Workflows disponibles
Workflow	Description
ci-pipeline.yml	Pipeline d'intégration continue (tests, build)
docker-build-push.yml	Build & push des images Docker
deploy-development.yml	Déploiement vers l'environnement développement
deploy-staging.yml	Déploiement vers l'environnement staging
📁 Branche utilisée
Tous les scripts CI/CD sont configurés et actifs sur la branche backend.

🔄 Commandes Git utilisées
git checkout backend
git add .
git commit -m "Add CI/CD pipeline with GitHub Actions"
git push origin backend