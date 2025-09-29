#!/usr/bin/env bash
set -euo pipefail

# Resolve repository root (this script is in docs/)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${REPO_ROOT}"

# Load .env if present at repo root
if [[ -f .env ]]; then
  echo "[env] Loading .env from repo root"
  # shellcheck disable=SC1091
  set -a; source ./.env; set +a
fi

# Defaults if not provided via environment or .env
DB_HOST="${DB_HOST:-localhost}"
DB_NAME="${DB_NAME:-trialarvi}"
DB_USER="${DB_USER:-postgres}"
DB_PASSWORD="${DB_PASSWORD:-root}"
SERVER_PORT="${SERVER_PORT:-8080}"

export DB_HOST DB_NAME DB_USER DB_PASSWORD SERVER_PORT

echo "[env] Using DB_HOST=$DB_HOST DB_NAME=$DB_NAME DB_USER=$DB_USER PORT=$SERVER_PORT"
echo "[check] Java:"; java -version || true
echo "[check] Maven:"; mvn -version || true

ACTION="${1:-run}"

case "$ACTION" in
  build)
    echo "[build] mvn clean package..."
    mvn clean package -DskipTests
    ;;
  run)
    echo "[build] mvn clean package..."
    mvn clean package -DskipTests
    echo "[run] Starting app on port $SERVER_PORT"
    exec java -jar target/3GAssociates-0.0.1-SNAPSHOT.jar --server.port="$SERVER_PORT"
    ;;
  docker-build)
    echo "[docker] Building image 3gassociates:latest"
    mvn clean package -DskipTests
    docker build -t 3gassociates .
    ;;
  docker-run)
    echo "[docker] Running 3gassociates on port $SERVER_PORT"
    docker run --rm -p "${SERVER_PORT}:8080" \
      -e DB_HOST -e DB_NAME -e DB_USER -e DB_PASSWORD \
      3gassociates
    ;;
  compose-up)
    echo "[compose] Starting docker-compose (all services)"
    docker compose up --build
    ;;
  compose-down)
    echo "[compose] Stopping docker-compose"
    docker compose down
    ;;
  *)
    echo "Usage: docs/env-setup.sh [build|run|docker-build|docker-run|compose-up|compose-down]" >&2
    exit 2
    ;;
esac
