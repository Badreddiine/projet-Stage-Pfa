# scripts/local-build.ps1
param(
    [string]$Service = "all"
)

Write-Host "🚀 Build local des services..." -ForegroundColor Cyan

$services = @("config-server", "discovery", "gateway")

if ($Service -eq "all") {
    $servicesToBuild = $services
} else {
    $servicesToBuild = @($Service)
}

foreach ($svc in $servicesToBuild) {
    Write-Host "📦 Build de $svc..." -ForegroundColor Yellow

    switch ($svc) {
        "config-server" {
            Set-Location "services/config server/config-server"
            & mvn clean package -DskipTests
        }
        "discovery" {
            Set-Location "services/discovery"
            & mvn clean package -DskipTests
        }
        "gateway" {
            Set-Location "services/gateway"
            & mvn clean package -DskipTests
        }
    }

    Set-Location $PSScriptRoot/..
    Write-Host "✅ $svc terminé" -ForegroundColor Green
}

Write-Host "🎉 Build terminé !" -ForegroundColor Green