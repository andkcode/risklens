CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20),
    is_active BOOLEAN,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

-- 2. Refresh tokens table
CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL
);

-- 3. Portfolios table
CREATE TABLE portfolios (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    base_currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

-- 4. Positions table
CREATE TABLE positions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    asset_symbol VARCHAR(20) NOT NULL,
    asset_type VARCHAR(10) NOT NULL,
    quantity NUMERIC(24, 8) NOT NULL CHECK (quantity > 0),
    avg_buy_price NUMERIC(24, 8) NOT NULL CHECK (avg_buy_price >= 0),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_portfolio_asset UNIQUE (portfolio_id, asset_symbol)
);

-- 5. Risk assessments table
CREATE TABLE risk_assessments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    portfolio_id UUID NOT NULL REFERENCES portfolios(id) ON DELETE CASCADE,
    calculated_at TIMESTAMPTZ NOT NULL,
    volatility NUMERIC(12, 6) NOT NULL,
    var_95 NUMERIC(12, 6) NOT NULL,
    max_drawdown NUMERIC(12, 6) NOT NULL,
    sharpe_ratio NUMERIC(12, 6) NOT NULL,
    risk_score SMALLINT NOT NULL
);

-- 6. Price history table
CREATE TABLE price_history (
    id bigserial PRIMARY KEY,
    asset_symbol VARCHAR(20) NOT NULL,
    trade_date DATE NOT NULL,
    open_price NUMERIC(18, 4),
    close_price NUMERIC(18, 4) NOT NULL,
    high_price NUMERIC(18, 4),
    low_price NUMERIC(18, 4),
    volume NUMERIC(24, 2),
    CONSTRAINT uk_asset_trade_date UNIQUE (asset_symbol, trade_date)
);