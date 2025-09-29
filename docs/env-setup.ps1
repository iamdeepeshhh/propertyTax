<#
.SYNOPSIS
  Environment setup and helper tasks for 3GAssociates.

.DESCRIPTION
  Loads .env from repo root if present, sets env vars, and can build/run locally or with Docker/Compose.

.PARAMETER Action
  One of: build, run, docker-build, docker-run, compose-up, compose-down. Default: run
#>

Param(
  [ValidateSet('build','run','docker-build','docker-run','compose-up','compose-down')]
  [string]$Action = 'run',
  [string]$DbHost = $env:DB_HOST,
  [string]$DbName = $env:DB_NAME,
  [string]$DbUser = $env:DB_USER,
  [string]$DbPassword = $env:DB_PASSWORD,
  [int]$ServerPort = $(if ($env:SERVER_PORT) { [int]$env:SERVER_PORT } else { 8080 })
)

# Move to repo root (this script is in docs/)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$RepoRoot = Resolve-Path (Join-Path $ScriptDir '..')
Set-Location $RepoRoot

# Load .env if present
$DotEnvPath = Join-Path $RepoRoot '.env'
if (Test-Path $DotEnvPath) {
  Write-Host "[env] Loading .env from repo root"
  Get-Content $DotEnvPath | ForEach-Object {
    if (-not [string]::IsNullOrWhiteSpace($_) -and -not $_.Trim().StartsWith('#')) {
      $idx = $_.IndexOf('=')
      if ($idx -gt 0) {
        $k = $_.Substring(0,$idx).Trim()
        $v = $_.Substring($idx+1).Trim()
        [Environment]::SetEnvironmentVariable($k, $v)
      }
    }
  }
}

# Defaults if not provided
if (-not $DbHost) { $DbHost = 'localhost' }
if (-not $DbName) { $DbName = 'trialarvi' }
if (-not $DbUser) { $DbUser = 'postgres' }
if (-not $DbPassword) { $DbPassword = 'root' }

$env:DB_HOST = $DbHost
$env:DB_NAME = $DbName
$env:DB_USER = $DbUser
$env:DB_PASSWORD = $DbPassword
$env:SERVER_PORT = $ServerPort

Write-Host "[env] Using DB_HOST=$($env:DB_HOST) DB_NAME=$($env:DB_NAME) DB_USER=$($env:DB_USER) PORT=$ServerPort"
Write-Host "[check] Java version:"; java -version
Write-Host "[check] Maven version:"; mvn -version

switch ($Action) {
  'build' {
    Write-Host "[build] mvn clean package..."
    mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) { throw 'Build failed' }
  }
  'run' {
    Write-Host "[build] mvn clean package..."
    mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) { throw 'Build failed' }
    Write-Host "[run] Launching Spring Boot app on port $ServerPort ..."
    & java -jar target/3GAssociates-0.0.1-SNAPSHOT.jar --server.port=$ServerPort
  }
  'docker-build' {
    Write-Host "[docker] Building image 3gassociates:latest"
    mvn clean package -DskipTests
    if ($LASTEXITCODE -ne 0) { throw 'Build failed' }
    docker build -t 3gassociates .
  }
  'docker-run' {
    Write-Host "[docker] Running 3gassociates on port $ServerPort"
    docker run --rm -p "$ServerPort:8080" `
      -e DB_HOST -e DB_NAME -e DB_USER -e DB_PASSWORD `
      3gassociates
  }
  'compose-up' {
    Write-Host "[compose] Starting docker-compose (all services)"
    docker compose up --build
  }
  'compose-down' {
    Write-Host "[compose] Stopping docker-compose"
    docker compose down
  }
}
