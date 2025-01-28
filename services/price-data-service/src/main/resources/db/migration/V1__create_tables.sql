
CREATE TABLE cryptocurrencies (
    id UUID PRIMARY KEY,
    symbol VARCHAR(255) UNIQUE NOT NULL,
    coin_gecko_id VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE prices (
    id UUID PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    price NUMERIC(19, 8) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (symbol) REFERENCES cryptocurrencies(symbol)
);