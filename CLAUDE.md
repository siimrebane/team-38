# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Borsibaar is a fullstack web application with a Spring Boot backend and Next.js frontend. The system is a stock exchange/trading platform ("börsibaari" in Estonian means "stock bar").

## Architecture

- **Backend**: Spring Boot 3.5.5 with Java 21, PostgreSQL database, Spring Security with OAuth2, JWT authentication
- **Frontend**: Next.js static export (HTML/CSS/JS), TypeScript, Tailwind CSS, Radix UI components
- **Database**: PostgreSQL with Liquibase migrations
- **Nginx**: Reverse proxy serving static frontend files and proxying `/api/` requests to the backend
- **Containerization**: Docker Compose for development and production

### Production Architecture

All traffic goes through a single nginx instance (same-origin, no CORS needed):

```
Client → Nginx (:80)
           ├── /api/*       → Backend (:8080)
           └── /*           → Static files (/var/www/html)
```

- Frontend is built as a static export (`npm run build` → `frontend/out/`)
- Static files are served from `/var/www/html` on the host, mounted into nginx
- Backend runs in a Docker container, not exposed publicly
- No separate frontend container in production

## Development Commands

### Local Development

```bash
# 1. Start backend + database
docker compose up

# 2. Start frontend dev server (in a separate terminal)
cd frontend && npm run dev
```

The frontend dev server on `:3000` proxies `/api/*` requests to the backend on `:8080` via Next.js rewrites (configured in `next.config.ts`).

### Backend (Spring Boot)

```bash
# Build backend
cd backend && ./mvnw clean package

# Run tests
cd backend && ./mvnw test
```

**IMPORTANT**: The backend runs inside Docker containers. Use `docker compose exec backend ./mvnw test` for running tests against the database.

### Frontend (Next.js)

```bash
# Development server with hot reload
cd frontend && npm run dev

# Build static export for production (outputs to frontend/out/)
cd frontend && npm run build

# Lint code
cd frontend && npm run lint
```

### Production Deployment

```bash
# Build and start production containers (postgres + backend + nginx)
docker compose -f docker-compose.prod.yaml up

# Build frontend static files
cd frontend && npm run build

# Copy static files to the host path mounted by nginx
cp -r frontend/out/* /var/www/html/
```

## Key Backend Architecture

The Spring Boot backend follows a layered architecture:

- **Controllers** (`controller/`): REST API endpoints under `/api/` prefix
- **Services** (`service/`): Business logic layer
- **Repositories** (`repository/`): Data access layer using Spring Data JPA
- **Entities** (`entity/`): JPA entities mapping to database tables
- **DTOs** (`dto/`): Request/Response data transfer objects
- **Mappers** (`mapper/`): MapStruct mappers for entity-DTO conversion
- **Config** (`config/`): Spring configuration classes

Key technologies:
- Spring Security with OAuth2 client (Google)
- JWT tokens for API authentication (cookie-based)
- Liquibase for database migrations
- MapStruct for object mapping
- Lombok for reducing boilerplate

### Authentication Flow

1. User clicks login → redirected to `/api/oauth2/authorization/google`
2. Google OAuth2 callback → `/api/login/oauth2/code/google`
3. Backend issues JWT cookie → redirects to `/api/auth/login/success`
4. Success handler redirects to frontend (`/dashboard/` or `/onboarding/`)
5. Frontend `useAuth` hook checks `/api/account` on each page load

## Frontend Structure

Next.js 15 application using the App Router with static export:

- **Pages**: `app/page.tsx` (landing), `app/(protected)/(sidebar)/dashboard/`, `app/(public)/login/`, `app/(protected)/onboarding/`
- **Auth Hook**: `hooks/useAuth.ts` — client-side authentication via `/api/account`
- **POS**: Point-of-sale view uses search params (`/pos?station=id`) instead of dynamic routes (required for static export)
- **Styling**: Tailwind CSS with custom components using Radix UI
- **TypeScript**: Fully typed with strict configuration

## Database

PostgreSQL database configured via Docker Compose. Migrations managed by Liquibase in `backend/src/main/resources/db/changelog/db.changelog-master.yaml`.

## Environment Setup

1. Copy `.sample.env` to `.env` and configure database credentials, Google OAuth2 keys, JWT secret
2. Start development: `docker compose up` + `cd frontend && npm run dev`

## Nginx Configuration

Single `nginx/nginx.conf` file handles:
- Static file serving from `/usr/share/nginx/html` (mounted from `/var/www/html`)
- API proxy to backend (`/api/` → `backend:8080`)
- SPA fallback (`try_files $uri $uri/index.html $uri.html /index.html`)
- Gzip compression
- Static asset caching (`/_next/static/`)

## Key Dependencies

### Backend
- Spring Boot Starter (Web, Data JPA, Security, OAuth2 Client)
- PostgreSQL driver and Liquibase
- JWT libraries (jjwt)
- MapStruct and Lombok
- Spring DotEnv for environment configuration

### Frontend
- Next.js 15 with Turbopack
- React 19
- Tailwind CSS v4
- Radix UI components
- TypeScript
