# scripts/health-check.ps1
Write-Host "Vérification de l'état des services..." -ForegroundColor Yellow

$services = @(
    @{Name="Config Server"; Url="http://localhost:8888/actuator/health"},
    @{Name="Discovery Service"; Url="http://localhost:8761/actuator/health"},
    @{Name="Gateway"; Url="http://localhost:8080/actuator/health"},
    @{Name="Keycloak"; Url="http://localhost:8181/health/ready"}
)

$allHealthy = $true

foreach ($service in $services) {
    try {
        $response = Invoke-RestMethod -Uri $service.Url -TimeoutSec 10
        Write-Host "✅ $($service.Name) : OK" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ $($service.Name) : NON DISPONIBLE" -ForegroundColor Red
        $allHealthy = $false
    }
}

if ($allHealthy) {
    Write-Host "Tous les services sont opérationnels ✅" -ForegroundColor Green
    exit 0
} else {
    Write-Host "Certains services ne sont pas disponibles ❌" -ForegroundColor Red
    exit 1
}