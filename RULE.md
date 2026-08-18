# Project Rules

This document defines the repository-level development rules for `kot-cloud`. It complements [AGENTS.md](AGENTS.md), which is the authoritative instruction set for contributors and automation working in this repository.

## Architecture

The system-module server follows DDD with a strict dependency direction:

```text
adapter -> application -> domain <- infrastructure
```

- `adapter` contains controllers, request/response DTOs, assemblers, validation, and transport concerns only.
- `application` coordinates use cases through commands, queries, handlers, and facades. It owns transaction boundaries but not domain rules.
- `domain` contains aggregates, value objects, repository interfaces, and business rules. It must not depend on Micronaut, adapter, or infrastructure code.
- `infrastructure` implements domain repository interfaces and owns ORM entities, persistence mappers, and external integrations.
- Application services depend on domain repository interfaces, never concrete infrastructure implementations.
- Cross-module calls use contracts exposed from an `*-api` module. Do not depend on server-module internals.

## Kotlin Conventions

- Use the existing package root: `com.arrocean.dev`.
- Use PascalCase for types, camelCase for functions and properties, and SCREAMING_SNAKE_CASE for constants.
- Keep code formatted with four spaces and the repository's Kotlin style.
- Prefer immutable `val` properties. Use `var` only when mutation is required by a framework or domain lifecycle.
- Keep functions focused. Extract helpers only when they remove meaningful complexity or are reused.
- Use Kotlin nullability and validation annotations instead of sentinel values for optional request data.
- Add comments only for non-obvious decisions, constraints, or algorithms. Do not narrate self-evident code.

## Naming and Placement

- Controllers: `XxxControllerV1`.
- Requests and responses: `XxxRequest` and `XxxResponse`; keep related DTOs in `XxxModels.kt` when that is the local pattern.
- Assemblers: `XxxAssembler`.
- Commands, queries, and handlers: `XxxCommand`, `XxxQuery`, `XxxCommandHandler`, and `XxxQueryHandler`.
- Application facades/services: `XxxAppService` or `XxxService`.
- Domain repositories belong under `domain/<feature>/repository`; implementations belong under `infrastructure/persistence/repositoryimpl`.
- ORM entities belong under `infrastructure/persistence/entity` and must not be reused as API DTOs or domain models.

## HTTP and API Rules

- Keep controllers thin: validate input, invoke an application service, and convert the result.
- Return `CommonResult<T>` for normal API responses. Use `ServiceException` and `ErrorCode` for expected business failures.
- Preserve the versioned route convention supplied by `ApiPrefix`, including `/v{n}/admin-api` and `/v{n}/app-api`.
- Use `@RequirePermission` for protected administrative operations. Menu visibility is not authorization.
- Document public endpoints and response schemas with the established Micronaut OpenAPI annotations.
- Do not alter runtime routes merely to make an API documentation import easier.

## RBAC and Tenant Constraints

- `super_admin` is database-operated only. No business API may create, update, assign, revoke, or delete it.
- Public administrator registration assigns the editable `system_admin` role.
- Do not cache effective RBAC permissions in a way that delays authorization changes without explicit invalidation. Current behavior reads persistence directly so changes take effect on the next request.
- `tenant_id` is present in models and schema as preparation. The current runtime behavior is single-tenant; do not add tenant filtering or isolation semantics without an explicit design decision.

## Persistence and Schema Changes

- PostgreSQL is the current supported persistence runtime.
- Keep domain repository interfaces independent of Micronaut Data and PostgreSQL implementation types.
- Update `sql/kot_cloud_system.sql` when a schema change requires a reproducible bootstrap path.
- Treat the SQL script as destructive: it drops and recreates system tables. Do not present it as an in-place migration mechanism.
- Add meaningful PostgreSQL table, column, and constraint comments for new system and RBAC schema objects.

## Security and Configuration

- Never commit passwords, database URLs with credentials, Redis credentials, JWT secrets, access tokens, refresh tokens, or private keys.
- Use environment variables for deployment-specific settings such as `R2DBC_URL`, `R2DBC_USER`, `R2DBC_PASSWORD`, `REDIS_URI`, and `JWT_SECRET`.
- Production `JWT_SECRET` values must be random and at least 32 bytes. The checked-in default is for local bootstrap only.
- Keep password handling behind the framework `PasswordEncoder` abstraction. Do not implement ad hoc hashing in business modules.
- Do not log raw passwords, authorization headers, access tokens, refresh tokens, session IDs, or connection strings containing credentials.

## OpenAPI and Apifox

- The source OpenAPI document is generated as OpenAPI 3.0.1.
- Run `:kot-module-system:kot-module-system-server:generateApifoxOpenApi` to generate the Apifox-oriented YAML.
- Import the generated `kot-cloud-apifox.yml` as OpenAPI 3.0 YAML. Do not convert it to OpenAPI 3.1.
- Before modifying Apifox project content, retrieve the existing standard API document with ID `443783480` in project `7595187`.

## Build and Verification

- Use the repository Gradle Wrapper.
- In this workspace, set `GRADLE_USER_HOME=D:\IDEA\.gradle`; do not use the default C-drive Gradle cache.
- Do not change dependency versions unless an explicit request or a demonstrated compatibility issue requires it.
- After code changes, run the narrowest relevant compilation, test, or build task. Documentation-only changes require diff validation rather than a full build.
- `ksp.incremental=false` is intentional because Micronaut OpenAPI aggregation currently fails with incremental PSI state enabled.

## Git Workflow

- Preserve unrelated changes in a dirty worktree. Do not reset, checkout, or revert changes you did not make.
- Use non-interactive Git commands.
- Do not commit, amend, push, or open a pull request unless explicitly requested.
- Use conventional commit messages in this format when committing:

  ```text
  [type](module): summary

  - Change summary one
  - Change summary two
  ```

- Common types include `feat`, `fix`, `docs`, `refactor`, `test`, `build`, and `chore`.
