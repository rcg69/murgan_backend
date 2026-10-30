# Murgan — E-commerce REST API

A REST API for an e-commerce application, built with Spring Boot, Java, Spring Data JPA, Spring Security, and PostgreSQL. It supports product discovery, customer accounts, cart operations, orders, ratings, and administrative workflows.

**Frontend:** https://murgan-ui.vercel.app  
**Frontend source:** [murgan-ui](https://github.com/rcg69/murgan-ui)

## Features

- User registration and authentication
- JWT bearer-token authentication and stateless request security
- Password hashing with BCrypt
- Product listing, product details, filtering, sorting, and pagination
- Category endpoints
- Authenticated cart retrieval and item add, update, and removal
- Cart checkout and customer order retrieval
- Admin dashboard and management endpoints for users, products, and categories
- Rating endpoints
- Request/response DTOs and centralized API exception handling

Protected cart operations resolve the current user from the authenticated security context rather than trusting a client-supplied user ID.

## Technology

- Java 17
- Spring Boot 3.3
- Spring Web
- Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- JJWT for JWT handling
- Lombok

## Architecture

The application separates HTTP handling, business logic, persistence, domain models, and security:

```text
src/main/java/com/murgan/ecommerce/
├── bootstrap/   # Initial data setup
├── domain/      # JPA entities
├── repository/  # Spring Data repositories
├── security/    # JWT filter, JWT service, user principal and security config
├── service/     # Business logic
└── web/
    ├── dto/     # Request and response DTOs
    └── *Controller.java
```

## API overview

| Area | Representative endpoints |
|---|---|
| Authentication | `POST /api/auth/register`, `POST /api/auth/login` |
| Products | `GET /api/products`, `GET /api/products/{id}` |
| Product search | `GET /api/products/search` |
| Categories | `GET /api/categories` |
| Cart | `GET /api/cart`, `POST /api/cart/items`, `PATCH /api/cart/items/{productId}`, `DELETE /api/cart/items/{productId}` |
| Checkout and orders | `POST /api/cart/checkout`, `GET /api/orders` |
| Administration | `/api/admin/**` |

Product search accepts optional category, text, price-range, stock, page, size, and sort parameters. Administrative endpoints require the administrator role; cart and order operations require authentication.

## Run locally

### Prerequisites

- JDK 17
- Maven
- PostgreSQL

Create a PostgreSQL database, for example `murgan`, then configure the application environment.

### Environment variables

The application reads the following variables:

| Variable | Purpose |
|---|---|
| `DATABASE_URL` | JDBC PostgreSQL connection URL |
| `SECURITY_JWT_SECRET` | A unique, strong signing secret |
| `SECURITY_JWT_ACCESS_TOKEN_TTL_SECONDS` | Access-token lifetime in seconds |
| `APP_CORS_ALLOWED_ORIGINS` | Comma-separated allowed frontend origins |
| `APP_BOOTSTRAP_ADMIN_ENABLED` | Whether initial admin bootstrap is enabled |
| `APP_BOOTSTRAP_ADMIN_EMAIL` | Initial administrator email |
| `APP_BOOTSTRAP_ADMIN_USERNAME` | Initial administrator username |
| `APP_BOOTSTRAP_ADMIN_PASSWORD` | Strong initial administrator password |

Set these in your local shell or deployment environment. Do not commit real credentials or signing secrets. Configure a strong JWT secret and unique bootstrap credentials before deployment; disable admin bootstrap after the initial account has been created.

### Start the API

```bash
git clone https://github.com/rcg69/murgan_backend.git
cd murgan_backend
mvn spring-boot:run
```

The server uses port `8080` by default. The port can be configured through the `PORT` environment variable.

### Run tests

```bash
mvn test
```

## Frontend

The companion Next.js application is available at [rcg69/murgan-ui](https://github.com/rcg69/murgan-ui). Configure its API base URL to point to this service.
