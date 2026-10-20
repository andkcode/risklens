# Architecture Overview — RiskLens

## System Components

1. **API Gateway / Security Layer**: Intercepts HTTP requests via `JwtAuthenticationFilter`, validates JWT tokens, and populates the `SecurityContextHolder`.
2. **Core Services**:
  - **AuthService**: Handles user registration, login, and token issuance.
  - **PortfolioService**: Manages user investment portfolios with ownership validation.
  - **PositionService**: Handles portfolio assets (stocks, crypto) with strict constraint checks.
  - **RiskCalculationService**: Computes financial risk parameters.
3. **Data Layer**: Spring Data JPA repositories interacting with PostgreSQL.

## Security Flow

```mermaid
sequenceDiagram
    participant Client
    participant API as Spring Security Filter
    participant Controller as REST Controller
    participant DB as PostgreSQL

    Client->>API: POST /api/auth/login (Credentials)
    API->>DB: Verify User
    DB-->>API: User Data
    API-->>Client: Return JWT Access Token
    
    Client->>API: POST /api/v1/portfolios (Bearer Token)
    API->>API: Validate JWT & Extract Claims
    API->>Controller: Forward Request with Principal
    Controller->>DB: Save/Fetch Portfolio
    DB-->>Controller: Result
    Controller-->>Client: 201 Created / 200 OK
