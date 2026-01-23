# Custom Authorization Server (OAuth2-style)

A learning-focused **Authorization Server** that issues JWT access tokens and DB-backed refresh tokens, publishes **JWKS** for verification, and demonstrates real-world IAM concepts: **client trust**, **token lifecycle**, **key rotation**, and **abuse hardening**.

> This project is not intended to replace enterprise IAM solutions (Keycloak/Okta).  
> It is a portfolio-grade implementation to understand how modern authentication systems work.

---

## Features

### Technology & Dependencies
- Java version (e.g., 21)
- Spring Boot version
- Spring Security usage scope (PasswordEncoder, later endpoint security)
- JWT library choice (Nimbus JOSE + JWT vs JJWT)
- DB: Postgres + Flyway
- Redis for rate limit

### Core
- OAuth2-style token endpoint: `POST /oauth2/token`
- JWT access tokens (signed, short-lived)
- DB-backed refresh tokens (opaque, rotated, revocable)
- JWKS endpoint: `GET /.well-known/jwks.json`
- Client registration (client_id + secret + allowed grants/scopes)

### Security
- Refresh token rotation + reuse detection
- Stores refresh tokens and client secrets as hashes (no raw secrets in DB)
- Rate limiting + lockout (phase 2)
- Audit logs for security-relevant events (phase 2)

### Optional (Roadmap)
- Client Credentials grant (service-to-service)
- Token introspection endpoint
- Authorization Code + PKCE (advanced)

---

### Documentation

- 📐 [System Design](DESIGN.md) — overall architecture, data model, and security design
- 🧠 [Architecture Decision Records](docs/adr) — key design decisions and their rationale
  - [ADR-001: JWT Signing Strategy](docs/adr/ADR-001-jwt-signing-strategy.md)

---

## Architecture (High Level)

```
User -> Client App -> Authorization Server (this project) -> issues tokens
|
| JWKS (public keys)
v
Resource Server(s) validate JWT
```

---

## Tech Stack

- Java + Spring Boot
- Spring Security
- PostgreSQL
- Flyway/Liquibase
- Redis (rate limiting)
- Docker / Docker Compose

---

## Quick Start

### 1) Configure environment
Create `.env` (or set env vars):

- `DB_URL=jdbc:postgresql://localhost:5432/authdb`
- `DB_USER=auth`
- `DB_PASSWORD=auth`
- `ISSUER=https://auth.local`
- `ACCESS_TOKEN_TTL_SECONDS=900`
- `REFRESH_TOKEN_TTL_SECONDS=2592000`

### 2) Run with Docker Compose
```bash
docker compose up -d
```

### 3) Run the app
```bash
./mvnw spring-boot:run
```

---

## API Overview

Base path: `/api`

Token endpoint
`POST /api/oauth2/token`

### Grant: password
Request (example):
```bash
curl -X POST http://localhost:8080/api/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "username=user@example.com" \
  -d "password=secret" \
  -d "client_id=my-client" \
  -d "client_secret=my-client-secret" \
  -d "scope=read write"
```
Response (example):
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6Ii4uLiJ9...",
  "token_type": "Bearer",
  "expires_in": 900,
  "refresh_token": "r1_opaque_refresh_token_value",
  "scope": "read write"
}
```
Grant: refresh_token
```bash
curl -X POST http://localhost:8080/api/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=refresh_token" \
  -d "refresh_token=r1_opaque_refresh_token_value" \
  -d "client_id=my-client" \
  -d "client_secret=my-client-secret"
```
> Refresh tokens are rotated. The old refresh token is revoked and replaced by a new one.

---

## JWKS

Resource servers validate JWT signatures using the published public keys.
`GET /api/.well-known/jwks.json`

Example:
```bash
curl http://localhost:8080/api/.well-known/jwks.json
```

---

## How to Use Tokens in a Resource Server

### What the Resource Server must validate

- Signature (using JWKS)
- `iss` equals configured issuer
- `aud` matches the API audience
- `exp` not expired
- Optional: `scop`e / `roles` claims to authorize endpoints

---

## Data Model (Minimal)

- `users`
- `clients`
- `refresh_tokens`
- `audit_log` (recommended)

Refresh tokens are stored as **hashes**. Raw refresh tokens are shown only once to the client.

---

## Security Notes (Practical)

- Access tokens are short-lived (5–15 minutes)
- Refresh tokens are long-lived and rotated on each use
- Reuse detection can revoke the entire refresh-token family
- Client secrets and refresh tokens are never stored in plaintext

---

## Project Structure

```perl
src/main/java/.../
  auth/         // user authentication, password hashing
  clients/      // client registration, validation, allowed grants
  tokens/       // jwt signing, refresh tokens, rotation, revocation
  keys/         // jwks publishing, rotation
  web/          // controllers, request validation
  audit/        // audit log events
```

## Roadmap

### Phase 1 (Core)

- [ ] Users + Clients
- [ ] /oauth2/token (password + refresh_token)
- [ ] JWT signing (RS256) + JWKS
- [ ] Refresh rotation + revocation

### Phase 2 (Hardening)

- [ ] Rate limiting + lockout on abuse
- [ ] Audit logs for security events
- [ ] Client Credentials grant

### Phase 3 (Advanced)

- [ ] Authorization Code + PKCE
- [ ] Well-known configuration endpoint
- [ ] Minimal admin UI (optional)

---

## Why This Project Exists

Enterprise IAM tools are great to use—but harder to understand deeply without building the core pieces:

- why JWKS exists,
- how key rotation works,
- how refresh tokens must be stored and rotated,
- how clients are trusted,
- and where authentication ends and authorization begins.

This repository is a practical deep-dive into those concepts.

---

## License

MIT
