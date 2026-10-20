CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 2. Refresh tokens table
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(512) UNIQUE NOT NULL,
    expiry_date TIMESTAMPTZ NOT NULL
);

-- 3. Portfolios table
CREATE TABLE portfolios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    base_currency CHAR(3) NOT NULL DEFAULT 'USD',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 4. Positions table (using 'ticker', CHECK quantity > 0, and UNIQUE(portfolio_id, ticker))
CREATE TABLE positions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    ticker VARCHAR(20) NOT NULL,
    asset_type VARCHAR(10) NOT NULL,
    quantity NUMERIC(18, 8) NOT NULL CHECK (quantity > 0),
    avg_buy_price NUMERIC(18, 4) NOT NULL CHECK (avg_buy_price >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_portfolio_ticker UNIQUE (portfolio_id, ticker)
);

-- 5. Risk assessments table
CREATE TABLE risk_assessments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    calculated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    volatility NUMERIC(10, 6) NOT NULL,
    var95 NUMERIC(10, 6) NOT NULL,
    max_drawdown NUMERIC(10, 6) NOT NULL,
    sharpe_ratio NUMERIC(10, 6) NOT NULL,
    risk_score SMALLINT NOT NULL
);

-- 6. Price history table
CREATE TABLE price_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticker VARCHAR(20) NOT NULL,
    price NUMERIC(18, 4) NOT NULL,
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);