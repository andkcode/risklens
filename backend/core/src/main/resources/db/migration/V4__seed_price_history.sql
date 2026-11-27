INSERT INTO price_history (
    asset_symbol,
    trade_date,
    open_price,
    high_price,
    low_price,
    close_price,
    volume
)
SELECT
    'AAPL',
    gs::date,
    180.0000,
    185.0000,
    178.0000,
    182.5000,
    1000000.00
FROM generate_series(
    (CURRENT_DATE - INTERVAL '60 days')::date,
    CURRENT_DATE,
    INTERVAL '1 day'
) AS gs
ON CONFLICT (asset_symbol, trade_date) DO NOTHING;