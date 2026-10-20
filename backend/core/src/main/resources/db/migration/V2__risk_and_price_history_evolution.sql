drop table if exists price_history cascade;

create table price_history (
    ticker varchar(20) not null,
    trade_date date not null,
    open_price numeric(18, 4),
    close_price numeric(18, 4) not null,
    high_price numeric(18, 4),
    low_price numeric(18, 4),
    volume numeric(24, 2),
    created_at timestamptz not null default now(),
    constraint pk_price_history primary key (ticker, trade_date)
);


drop table if exists risk_assessments cascade;

create table risk_assessments (
    id uuid primary key default uuid_generate_v4(),
    portfolio_id uuid not null references portfolios(id) on delete cascade,
    calculated_at timestamptz not null default now(),
    volatility numeric(10, 6) not null,
    var95 numeric(10, 6) not null,
    max_drawdown numeric(10, 6) not null,
    sharpe_ratio numeric(10, 6) not null,
    risk_score smallint not null check (risk_score >= 0 and risk_score <= 100),

    -- Allowed values constraints for method and status
    method varchar(30) not null default 'HISTORICAL'
        check (method in ('HISTORICAL', 'MONTE_CARLO', 'PARAMETRIC')),

    status varchar(20) not null default 'COMPLETED'
        check (status in ('PENDING', 'COMPLETED', 'FAILED'))
);
