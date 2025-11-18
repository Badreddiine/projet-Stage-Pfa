# scripts/start-local.ps1
Write-Host "🐳 Démarrage de l'environnement local..." -ForegroundColor Cyan

# Vérification de Docker
try {
    docker --version | Out-Null
} catch {
    Write-Host "❌ Docker n'est pas disponible" -ForegroundColor Red
    exit 1
}

# Build des images si nécessaire
Write-Host "🔨 Build des images Docker..." -ForegroundColor Yellow
docker-compose build

# Démarrage des services
Write-Host "🚀 Démarrage des conteneurs..." -ForegroundColor Yellow
docker-compose up -d

# Attente et vérification
Write-Host "⏳ Attente du démarrage des services..." -ForegroundColor Yellow
Start-Sleep 30

# Vérification de l'état
Write-Host "🔍 Vérification de l'état des services..." -ForegroundColor Yellow
& "$PSScriptRoot/health-check.ps1"

Write-Host "🎉 Environnement local prêt !" -ForegroundColor Green
Write-Host "📍 Accès aux services:" -ForegroundColor Cyan
Write-Host "   - Config Server: http://localhost:8888" -ForegroundColor White
Write-Host "   - Discovery: http://localhost:8761" -ForegroundColor White
Write-Host "   - Gateway: http://localhost:8080" -ForegroundColor White
Write-Host "   - Keycloak: http://localhost:8181" -ForegroundColor White
Write-Host "   - pgAdmin: http://localhost:5050" -ForegroundColor White