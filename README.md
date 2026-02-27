# E-commerce Spring Boot Backend (PostgreSQL + JWT)

## Tech
- Spring Boot 3 (Java 17)
- PostgreSQL
- Spring Security (JWT Bearer)
- Spring Data JPA

## Run (local)
1. Create a PostgreSQL database (example: `ecommerce`).
2. Set environment variables (PowerShell examples):

```bash
$env:DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"

# MUST be >= 32 chars
$env:JWT_SECRET="replace-this-with-a-long-random-secret-32-chars-min"

# CORS (frontend)
$env:CORS_ALLOWED_ORIGINS="http://localhost:3000"
```

3. Start the API:

```bash
mvn spring-boot:run
```

## Bootstrap an admin (optional)
By default, the app **does not** auto-create an admin user.

To create a first admin on startup:

```bash
$env:BOOTSTRAP_ADMIN_ENABLED="true"
$env:BOOTSTRAP_ADMIN_EMAIL="admin@example.com"
$env:BOOTSTRAP_ADMIN_USERNAME="admin"
$env:BOOTSTRAP_ADMIN_PASSWORD="ChangeThisStrongPassword!"
```

Start the app once, login, then disable bootstrap again.

## Auth
- **Register**: `POST /api/auth/register`
- **Login**: `POST /api/auth/login` → returns `accessToken`

Send token to protected routes:
`Authorization: Bearer <token>`

## Core endpoints (frontend)
- **Products (fast list)**: `GET /api/products?page=0&size=20&sort=createdAt,desc`
- **Products (filter/sort/search)**: `GET /api/products/search?categoryId=1&q=phone&minPrice=100&maxPrice=500&inStock=true&sort=price,asc`
- **Categories**: `GET /api/categories`

## Cart (requires login)
- `GET /api/cart`
- `POST /api/cart/items`
- `PATCH /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/cart/checkout`

Cart data is always tied to the **JWT-authenticated user** (no userId is accepted from the client).

## Orders (requires login)
- `GET /api/orders`

## Admin (requires ROLE_ADMIN)
- Dashboard: `GET /api/admin/dashboard`
- Users: `GET /api/admin/users`
- Categories: `POST/PUT/DELETE /api/admin/categories`
- Products: `POST/PUT/DELETE /api/admin/products`

