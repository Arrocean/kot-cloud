# Security Policy

## Supported Scope

Security fixes are applied to the current development branch and the latest published project state. This repository does not currently maintain multiple supported release lines.

- Supported: current development branch.
- Not supported: historical commits and unpublished forks.

## Reporting a Vulnerability

Please report suspected vulnerabilities privately to the repository maintainers through the hosting platform's private security-reporting channel when available. If private reporting is unavailable, contact the maintainers directly rather than opening a public issue.

Do not include proof-of-concept exploits, secrets, personal data, access tokens, or production endpoints in public issues, pull requests, commits, or comments.

Include the following in a report when possible:

- A concise description of the affected component and impact.
- Reproduction steps or a minimal proof of concept.
- Affected versions, commit IDs, or deployment conditions.
- Suggested mitigation, if known.

Maintainers will acknowledge the report, assess reproducibility and severity, coordinate a fix when appropriate, and disclose the result after a remediation plan is available.

## Security Baseline

- Authentication uses Micronaut Security bearer tokens and JWT access tokens.
- Session state is stored in Redis; production Redis connections must be private and authenticated.
- Administrative authorization is enforced server-side with RBAC and `@RequirePermission`. Client-side menu visibility is not an authorization control.
- `super_admin` is a database-operated bootstrap role. It must not be exposed through role-management APIs.
- Passwords are stored through the framework `PasswordEncoder` abstraction. Supported encoders include Argon2id, BCrypt, and PBKDF2-HMAC-SHA256.
- Effective permissions are currently resolved from persistence so RBAC changes apply on the next request. JWTs carry identity and session information and must not be treated as the sole source of current permissions.

## Deployment Requirements

- Set a random `JWT_SECRET` of at least 32 bytes outside source control. Never deploy with the checked-in fallback value.
- Provide `JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD`, and `REDIS_URI` through deployment secrets or environment-specific configuration.
- Use TLS for public ingress and HTTPS for administrative clients and external menu targets.
- Restrict PostgreSQL and Redis network access to application workloads and authorized operators.
- Rotate credentials and JWT signing secrets after suspected disclosure. Rotation invalidates existing sessions unless a coordinated transition is implemented.
- Do not expose Swagger/OpenAPI endpoints publicly unless that access is intentional and protected by deployment controls.

## Secure Development Requirements

- Never commit credentials, tokens, private keys, production database dumps, or local configuration containing secrets.
- Do not log passwords, authorization headers, JWTs, refresh tokens, session IDs, or connection strings containing credentials.
- Use parameterized Micronaut Data/JDBC access; do not construct SQL with untrusted string concatenation.
- Validate external URLs used by menu entries. External and iframe targets must use HTTPS and be restricted by an allowlist in the consuming client.
- Treat `sql/kot_cloud_system.sql` as a destructive bootstrap script. Review it carefully before execution because it drops and recreates system tables.
- Keep dependency updates deliberate. Do not upgrade versions opportunistically without compatibility verification.

## Out of Scope

- Vulnerabilities in modified local environments, unreviewed forks, or unsupported historical revisions.
- Availability issues caused by intentionally running the destructive bootstrap SQL against a populated database.
- Security controls that are expected to be supplied by a production deployment but are absent from the current Docker Compose skeleton, such as TLS termination, secret management, database provisioning, and network policy.
