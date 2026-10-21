create index idx_portfolios_user_id on portfolios(user_id);

create index idx_positions_portfolio_id on positions(portfolio_id);

create index idx_risk_assessments_portfolio_created on risk_assessments(portfolio_id, calculated_at desc);

create index idx_price_history_asset_date on price_history(asset_symbol, trade_date desc);