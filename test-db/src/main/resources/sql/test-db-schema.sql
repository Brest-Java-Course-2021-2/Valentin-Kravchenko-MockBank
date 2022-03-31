CREATE TABLE bank_account
(
    id                SERIAL PRIMARY KEY,
    number            VARCHAR(34)  NOT NULL UNIQUE,
    customer          VARCHAR(128) NOT NULL,
    registration_date DATE         NOT NULL
);
CREATE TABLE credit_card
(
    id              SERIAL PRIMARY KEY,
    number          VARCHAR(16) NOT NULL UNIQUE,
    expiration_date DATE        NOT NULL,
    balance         DECIMAL(8, 2) DEFAULT 0,
    account_id      INT REFERENCES bank_account (id)
);