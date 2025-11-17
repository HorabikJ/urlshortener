# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a URL shortening service built with Spring Boot 3.4.1 and Java 21. It uses Keycloak for OAuth2/OIDC
authentication, MySQL for persistence, and Thymeleaf for server-side rendering. The application can be deployed locally,
on Kubernetes, or AWS EC2.

## Build and Development Commands

### Running Tests

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=UrlShorteningServiceIntegrationTest

# Run a specific test method
mvn test -Dtest=UrlShorteningServiceIntegrationTest#testShortenUrl
```

### Building the Application

```bash
# Clean and build
mvn clean install

# Build without running tests
mvn clean install -DskipTests

# Package into executable JAR
mvn package
```

### Running the Application

**Local Development (with Docker dependencies):**

```bash
# Start MySQL and Keycloak services
docker compose up -d

# Run the application with 'local' profile (from IDE or command line)
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Application runs on http://localhost:8081
# Keycloak runs on http://localhost:8080
```

**Using H2 Database (no Docker required):**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**Kubernetes Deployment:**

```bash
# Build images and deploy to k8s cluster
./k8s-run.sh

# Application accessible on NodePort 30008
# Keycloak accessible on NodePort 30009
```

### Database Management

**Liquibase Commands:**

```bash
# Generate changelog from existing database
mvn liquibase:generateChangeLog

# Update database to latest version
mvn liquibase:update

# Rollback last change
mvn liquibase:rollback
```

Note: Liquibase plugin in pom.xml is configured for local MySQL (127.0.0.1:3306).

### Stopping Services

```bash
# Stop Docker services
docker compose down

# Delete Kubernetes deployment
kubectl delete namespace urlshortener-dev
```

## Architecture and Code Structure

### High-Level Architecture

This is a monolithic Spring Boot application following a layered architecture:

- **Presentation Layer**: Thymeleaf templates + Controllers (MainPageController, RedirectController)
- **Service Layer**: Business logic (UrlShorteningService)
- **Persistence Layer**: JPA Repositories + Entities (UrlEntity, UrlRepository)
- **Database**: MySQL (production) or H2 (testing)
- **External Auth**: Keycloak OAuth2/OIDC server

### Package Structure

Code is organized by feature in the `pl.jacekhorabik.urlshortener` package:

**`shortenurl`** - Core URL shortening business domain

- `MainPageController` - Handles GET/POST for URL shortening
- `RedirectController` - Redirects shortened URLs to original URLs
- `UrlShorteningService` - Implements the URL shortening algorithm
- `UrlEntity` - JPA entity (hash as primary key)
- `UrlRepository` - Data access layer
- `UrlDTO` - Data transfer object
- `UrlShorteningConfig` - Base62 encoder configuration

**`security`** - Authentication and authorization

- `SecurityConfig` - OAuth2 login/logout configuration
- `AuthoritiesConverter` - Converts Keycloak realm roles to Spring authorities
- `AuthoritiesMapper` - Additional role mapping

**`security.aspects`** - AOP for cross-cutting concerns

- `AuthorizationAspect` - Injects user authentication data into ModelAndView
- `@AddUserDataToModel` - Annotation to trigger the aspect

**`common`** - Shared utilities

- `common.entity.BaseEntity` - Base entity with createdAt/updatedAt timestamps
- `common.security.UserRole` - Role enum (ADMIN, USER)
- `common.viewname.ViewName` - View name constants

**`exception`** - Global exception handling

- `GlobalExceptionHandler` - @ControllerAdvice for 404s and other exceptions

**`health`** - Custom health indicators

- `CustomDataSourceHealthIndicator` - Database connectivity health check

**`test`** - Test endpoints

- `TestController` - Admin/user role testing endpoints

### URL Shortening Algorithm

The core algorithm in `UrlShorteningService.java`:

1. Takes input URL
2. Computes SHA-1 hash of the URL
3. Encodes hash bytes using Base62
4. Takes first 7 characters as short hash
5. Checks for hash collision in database
6. If collision exists, appends UUID and recursively retries
7. Saves UrlEntity with hash as primary key
8. Returns shortened URL: `{baseUrl}/v1/r/{hash}`

### Configuration Profiles

The application uses Spring profiles for environment-specific configuration:

- **`local`** - Local development (port 8081, localhost URLs, local MySQL)
- **`k8s`** - Kubernetes deployment (port 8080, service-based URLs, NodePort access)
- **`aws`** - AWS EC2 deployment (port 80, hardcoded IP addresses)
- **`h2`** - H2 in-memory database (for development without Docker)
- **`test`** - Test configuration (H2 database, Liquibase disabled, mock Keycloak)

Configuration files are in `src/main/resources/`:

- `application.yml` - Base configuration
- `application-{profile}.yml` - Profile-specific overrides

### Database Schema

Managed by Liquibase changelogs in `src/main/resources/db/changelog/`:

- `00_changelog-master.xml` - Master changelog
- `01_urls-table.xml` - Creates `urls` table

**urls table:**

- `hash` VARCHAR(7) - Primary key, the shortened URL hash
- `url` VARCHAR(10000) - Original URL
- `created_at` TIMESTAMP
- `updated_at` TIMESTAMP

### Security and Endpoints

**Authentication**: OAuth2/OIDC via Keycloak with Authorization Code flow

**Authorization**:

- `/actuator/health` - Public (health checks)
- `/v1/**` - Public (URL shortening and redirection)
- `/admin` - Requires ADMIN role
- `/user` - Requires USER role
- `/logout` - Authenticated users only

**Main Endpoints**:

- `GET /v1/` - Main page with URL shortening form
- `POST /v1/` - Submit URL for shortening
- `GET /v1/r/{hash}` - Redirect to original URL by hash
- `GET /actuator/health` - Health check endpoint

### Testing Setup

Tests use:

- JUnit 5 (Jupiter)
- Spring Boot Test with `@SpringBootTest`
- H2 for test database
- Spring Security Test
- Spring Addons OAuth2 Test for mocking authentication
- `@ActiveProfiles("test")` for test profile
- `@Sql` annotations for test data setup

Integration tests use `@AutoConfigureTestDatabase(replace = NONE)` to test against real database behavior.

### Template Fragments

Thymeleaf templates use fragment composition for reusability. Fragments are located in
`src/main/resources/templates/fragments/`:

- `head-metadata.html` - HTML head with common metadata
- `navbar.html` - Navigation bar
- `bootstrap-scripts.html` - Bootstrap JS scripts

Use `th:replace` or `th:insert` to include fragments in templates.

### Known TODOs and Planned Improvements

The codebase contains several TODO comments for future enhancements:

- Add Hibernate validation to UrlEntity
- Implement comprehensive exception handling (500 errors, etc.)
- Add URL validation (format validation, prevent self-referencing URLs)
- Use Testcontainers for integration tests
- Secure Kubernetes probe endpoints
- Add unique constraints on hash+url combination in database
- Replace sleep commands with readiness probes in k8s-run.sh

### Multi-Environment Deployment

**Local**: Docker Compose with MySQL and Keycloak containers
**Kubernetes**: Three-tier deployment (MySQL, Keycloak, Backend) in `urlshortener-dev` namespace
**AWS EC2**: User data startup script in `aws/urlshortener-user-data-startup-script.sh`

Each environment requires:

1. Keycloak realm configuration (generated via scripts in `docker-containers/keycloak/{env}/`)
2. Appropriate Spring profile activation
3. Correct URL configuration for issuer-uri and redirect-uri

### Development Workflow

1. Start dependencies: `docker compose up -d`
2. Run application from IDE with `local` profile
3. Access application at http://localhost:8081/v1/
4. Access Keycloak admin console at http://localhost:8080 (admin/admin)
5. Make code changes and hot-reload via IDE
6. Run tests: `mvn test`
7. Stop dependencies: `docker compose down`
