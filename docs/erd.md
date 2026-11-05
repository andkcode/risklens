```mermaid
erDiagram
    users {
        UUID id PK
        VARCHAR email UK
        VARCHAR password_hash
        TIMESTAMPTZ created_at
    }
    
    refresh_tokens {
        UUID id PK
        UUID user_id FK
        VARCHAR token UK
        TIMESTAMPTZ expiry_date
    }

    portfolios {
        UUID id PK
        UUID user_id FK
        VARCHAR name
        CHAR(3) base_currency
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    positions {
        UUID id PK
        UUID portfolio_id FK
        VARCHAR asset_symbol
        VARCHAR asset_type
        NUMERIC quantity
        NUMERIC avg_buy_price
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    risk_assessments {
        UUID id PK
        UUID portfolio_id FK
        TIMESTAMPTZ calculated_at
        NUMERIC volatility
        NUMERIC var95
        NUMERIC max_drawdown
        NUMERIC sharpe_ratio
        SMALLINT risk_score
    }

    price_history {
        UUID id PK
        VARCHAR asset_symbol
        NUMERIC price
        TIMESTAMPTZ recorded_at
    }

    users ||--o{ portfolios : owns
    users ||--o{ refresh_tokens : has
    portfolios ||--o{ positions : contains
    portfolios ||--o{ risk_assessments : evaluated_by
