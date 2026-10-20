# Deployment & Operations Guide — RiskLens

This guide outlines containerized deployment using Docker Compose.

## Services & Ports

- **PostgreSQL**: Port `5432` (Persistent storage via `postgres_data` volume).
- **Redis**: Port `6379` (Persistent storage via `redis_data` volume).
- **App**: Port `8080` (Spring Boot application with health checks).

## Health Checks & Startup Order

The `app` container automatically waits for both `postgres` and `redis` to pass their internal health checks (`pg_isready` and `redis-cli ping`) before starting up.

## Useful Commands

- **Start services**: `docker-compose up --build -d`
- **View logs**: `docker-compose logs -f app`
- **Stop services**: `docker-compose down`
- **Clean volumes (Reset DB)**: `docker-compose down -v`
