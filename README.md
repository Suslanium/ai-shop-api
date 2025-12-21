# ShopAPI

## Technical description
ShopAPI is a Spring Boot 2.6 (Java 17) REST service that manages a simple catalog of computer hardware:
hard drives, laptops, monitors, and personal computers. It exposes CRUD-style endpoints for each product
type and includes lightweight analytics endpoints for inventory counts and in-memory endpoint usage stats.
The service uses PostgreSQL via Spring Data JPA.

This project and its infrastructure were created with the help of GPT-5.2-Codex as part of AI Subject
Laboratory Work.

## Setup guide

### Prerequisites
- Docker with Docker Compose v2

### Local run (app + database)
```
docker compose -f docker-compose.local.yml up --build
```

The API will be available at:
- http://localhost:8080

### Run tests in Docker
```
docker compose -f docker-compose.test.yml up --build --abort-on-container-exit --exit-code-from tests
```

### Environment variables
The container setup uses these defaults:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/shop`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=shopAPI`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

## Architecture overview

### Layers
- **Controllers**: REST endpoints in `src/main/java/testtask/shift/shopapi/controller`.
- **Services**: Business logic in `src/main/java/testtask/shift/shopapi/service`.
- **Repositories**: Spring Data JPA repositories in `src/main/java/testtask/shift/shopapi/repository`.
- **Models**: JPA entities in `src/main/java/testtask/shift/shopapi/model`.
- **Config**: Web configuration in `src/main/java/testtask/shift/shopapi/config`.

### Request flow
HTTP request -> Controller -> Service -> Repository -> PostgreSQL

### Analytics
Analytics endpoints are computed at runtime:
- Inventory counts are pulled from repository `count()` methods.
- Endpoint usage stats are stored in-memory by a request interceptor.

## API overview

### Product endpoints
Base URLs:
- `/api/hdds`
- `/api/laptops`
- `/api/monitors`
- `/api/pcs`

Common operations:
- `GET /api/<type>` - list items
- `GET /api/<type>/{id}` - get item by id
- `POST /api/<type>/add` - create item
- `PUT /api/<type>/{id}` - update item (id is forced to the path id)

### Analytics endpoints
- `GET /api/analytics/counts`
  - Returns total and per-category counts.
- `GET /api/analytics/usage`
  - Returns endpoint usage counts since the app started.

### Swagger UI
API documentation is available at:
- `http://localhost:8080/swagger-ui/index.html`

## Data model summary
All product types share common fields:
- `id` (generated)
- `seriesNumber`
- `producer`
- `price`
- `numberOfProductsInStock`

Type-specific fields:
- HardDrive: `capacity`
- Laptop: `size` (enum)
- Monitor: `diagonal`
- PersonalComputer: `formFactor` (enum)

## Development notes
- The project targets Java 17.
- Default database is PostgreSQL; schema is auto-created/updated on start.
- Analytics usage stats are in-memory and reset on restart.

## CI/CD
GitHub Actions workflow builds and tests via Docker Compose, then pushes an image to GHCR and can deploy
to a VPS via Docker Compose when secrets are configured.
