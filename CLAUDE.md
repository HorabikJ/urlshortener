# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Running

```bash
# Build the project
mvn clean install

# Run the application (requires MySQL and Keycloak running)
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Start dependencies via Docker Compose
docker-compose up -d
```

### Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UrlShorteningServiceIntegrationTest

# Run specific test method
mvn test -Dtest=UrlRepositoryTest#testFindByUserId
```

### Database Management

```bash
# Generate changelog from existing database
mvn liquibase:generateChangeLog

# Update database to latest version
mvn liquibase:update

# Rollback last changeset
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## Architecture Overview

### URL Shortening Algorithm

The application uses **SHA1 hashing + Base62 encoding** for URL shortening:

- Original URL → SHA1 hash → Base62 encoded → First 7 characters = short hash
- The hash serves as the primary key in the database
- Collision detection: If hash exists, retry with UUID appended to URL
- Located in `ShortenUrlService.java:shortenUrl()`

### Package Organization

The codebase follows a **feature-based** (not layer-based) structure:

```
pages/
├── mainpage/          # URL shortening feature
├── useraccountpage/   # User account management
├── common/            # Shared entities, DTOs, views
└── testpage/          # Test endpoints
```

Each feature contains its own controllers, services, and DTOs. This improves cohesion and makes features easier to
locate.

### Authentication Pattern: AOP-Based Injection

The app uses **AspectJ** to automatically populate user authentication context:

1. Controllers annotate methods with `@PopulateUserAuthentication`
2. `PopulateUserAuthenticationAspect` intercepts these methods
3. Extracts OAuth2 token claims (`sub`, `preferred_username`) from SecurityContext
4. Populates `UserAuthentication` parameter automatically

This eliminates boilerplate auth extraction in every controller method.

## Security Architecture

### OAuth2 + Keycloak Integration

- **Identity Provider**: Keycloak (version 25.0.3)
- **Flow**: Authorization Code Grant with OIDC
- **Token Claims**: `sub` (user ID), `preferred_username`, `realm_access.roles`

### Authority Mapping Pipeline

```
Keycloak Token (realm_access.roles)
    ↓
AuthoritiesConverter (extracts roles from JWT)
    ↓
AuthoritiesMapper (converts to Spring GrantedAuthority)
    ↓
SecurityContext (used for authorization checks)
```

### Access Control

- **Public endpoints**: `/v1/r/{hash}` (redirect), `/actuator/health`, static resources
- **Authenticated**: `/v1/url/create`, `/v1/url/delete`, `/logout`
- **User role required**: `/v1/account/**`
- **Admin role required**: `/admin` (currently unused)

### Keycloak Admin Client

`UserAccountService` uses Keycloak Admin Client to:

- Fetch user account info (username, email, creation date)
- Delete user accounts (cascades to their URLs)
- Configuration in `KeycloakAdminConfig`

## Database Architecture

### Schema Management

- **Migration Tool**: Liquibase
- **Changelog Location**: `src/main/resources/db/changelog/`
- **Master File**: `00_changelog-master.xml`
- **DDL Mode**: `validate` (schema must match migrations; no auto-creation)

### Entity Design

```
BaseEntity (@MappedSuperclass)
├─ createdAt: Instant  (database-generated)
└─ updatedAt: Instant  (database-generated)
     ↓
UrlEntity
├─ hash: String (@Id)     # 7-char Base62 string (PK)
├─ url: String            # Original URL (max 10,000 chars)
└─ userId: String         # Optional Keycloak user ID
```

### Databases

- **Production**: MySQL 8.0
- **Testing**: H2 in-memory
- **Liquibase User**: `urlshortener-admin` (for migrations)
- **Application User**: `urlshortener-user` (reduced privileges)

## Frontend Architecture

### Thymeleaf Templates

- **Location**: `src/main/resources/templates/`
- **Main Views**: `main-page.html`, `account-info.html`, `not-found.html`
- **Fragments**: `fragments/` (navigation, head metadata, Bootstrap scripts)

### View Configuration

Views are defined in `View` enum with paths and template names. Controllers reference these for consistency.

### Model Attributes

- Use `AttributeName` enum for model attribute keys
- Common attributes: `requestUrlDTO`, `responseUrlDTO`, `userUrlsPagesDTO`, `userAuthentication`

## Configuration Profiles

The application supports multiple deployment environments:

- **local**: Docker Compose (MySQL on localhost:3306, Keycloak on localhost:8080)
- **k8s**: Kubernetes with service discovery and NodePort access
- **aws**: EC2 instances with direct IP addressing
- **h2**: H2 in-memory database for testing

### URL Configuration Pattern

```yaml
keycloak:
  external-base-url: [ user-facing URL ]    # Browser redirects
  internal-base-url: [ service-to-service ] # Token validation

app:
  external-base-url: [ public app URL ]     # Shortened URL construction
```

This separation handles Docker networking, Kubernetes DNS, and AWS private/public IPs.

## Key Service Responsibilities

### ShortenUrlService

- `shortenUrl()`: Creates shortened URLs with collision handling
- `findUrlByHash()`: Retrieves original URL for redirection
- `findUrlsByUserId()`: Paginated listing of user's URLs
- `deleteUserUrlByHash()`: Deletes URL with ownership verification

### UserAccountService

- `fetchUserAccountInfo()`: Retrieves user data from Keycloak + URL count
- `deleteUserAccount()`: Cascading deletion (user + all their URLs)

### RedirectController

- `GET /v1/r/{hash}`: Public redirect endpoint (no auth required)
- Returns 302 redirect to original URL or 404 if not found

## Important Development Notes

### When Adding New Features

1. Follow the feature-based package structure (`pages/{feature-name}/`)
2. Use `@PopulateUserAuthentication` for controllers needing user context
3. Define views in `View` enum and attributes in `AttributeName` enum
4. Create Liquibase changesets for schema changes (never use `ddl-auto: update`)

### Security Considerations

- URL validation incomplete: Currently doesn't prevent shortening of app's own domain
- CSRF protection configured but may need adjustment for production
- All POST endpoints require authentication except public redirects

### Database Patterns

- `open-in-view: false` is set to prevent lazy loading issues
- Use explicit `@Transactional` boundaries in services
- Primary keys on `UrlEntity` are the hash values themselves (immutable)
- Consider adding indices on `user_id` and `created_at` for large datasets

### Testing

- Integration tests use H2 with `application-test.yml` profile
- `@WithMockOAuth2User` annotation for testing authenticated endpoints
- Test resources in `src/test/resources/`
