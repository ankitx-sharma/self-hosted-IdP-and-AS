# ADR-001: JWT Signing Strategy

## Status
Accepted

## Context
The Authorization Server issues access tokens that must be validated by one or more
Resource Servers.

The system must support:
- Independent token validation by Resource Servers
- No sharing of private secrets between services
- Future key rotation without invalidating all active tokens
- Compatibility with standard JWT libraries

## Decision
Use asymmetric JWT signing with **RS256** and publish public keys via a **JWKS endpoint**.

- Access tokens are signed using an RSA private key
- The corresponding public keys are exposed at `/.well-known/jwks.json`
- Each JWT includes a `kid` header to support key rotation

## Alternatives Considered
- **HS256 (shared secret)**  
  Rejected because all Resource Servers would need access to the signing secret,
  increasing blast radius in case of compromise.

- **Opaque access tokens with introspection**  
  Rejected because it requires every Resource Server request to call the Authorization
  Server, increasing latency and coupling.

## Consequences
- Resource Servers can validate tokens locally using JWKS
- Key rotation can be performed without downtime
- Slightly more complex key management than symmetric signing
- Aligns with common OAuth2/OIDC production patterns

## Related documents:
- [README](../../README.md)
- [DESIGN.md](../../DESIGN.md)
