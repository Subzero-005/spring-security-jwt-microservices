# Spring Security JWT Microservices

Role-based access control across Spring Boot microservices. An auth service signs JWTs, a Spring Cloud Gateway routes requests, and each downstream service checks the token and the caller's role on its own.

## Architecture

```
browser / client.html
        │
        ▼
  api-gateway :8080  ── routes + CORS
   ├── /auth/**  ──► auth-service  :8083  issues HS256 JWTs (jjwt)
   ├── /user/**  ──► user-service  :8081  requires ROLE_USER
   └── /admin/** ──► admin-service :8082  requires ROLE_ADMIN
```

The user and admin services are stateless OAuth2 resource servers. They check the JWT signature with a shared secret and map the `role` claim to Spring Security authorities.

## Tech stack

Java 21 · Spring Boot 3.3 · Spring Cloud Gateway (2023.0) · Spring Security (OAuth2 Resource Server) · JJWT 0.12 · Maven

## Running it

Start each service in its own terminal:

```bash
cd auth-service  && ./mvnw spring-boot:run
cd user-service  && ./mvnw spring-boot:run
cd admin-service && ./mvnw spring-boot:run
cd api-gateway   && ./mvnw spring-boot:run
```

Then open `client.html` in a browser, or use curl:

```bash
ADMIN=$(curl -s -X POST 'localhost:8080/auth/login?username=admin')
USER=$(curl -s -X POST 'localhost:8080/auth/login?username=john')

curl localhost:8080/admin -H "Authorization: Bearer $ADMIN"   # 200 Hello ADMIN
curl localhost:8080/admin -H "Authorization: Bearer $USER"    # 403
curl localhost:8080/user  -H "Authorization: Bearer $USER"    # 200 Hello USER
curl localhost:8080/user                                      # 401
```

The username `admin` gets `ROLE_ADMIN`, and any other username gets `ROLE_USER`.

## Notes

- This is a demo of how the auth flow works. Login takes only a username, with no password check, so anyone can get a token. A real system would check credentials against a user store.
- The JWT secret in `application.properties` is a placeholder. Override it with the `JWT_SECRET` environment variable (Spring maps it to `jwt.secret`) before using this anywhere real.
