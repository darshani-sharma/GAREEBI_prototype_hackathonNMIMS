-- V1: Initial Schema for GAREEBI energy trading platform

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Enum Types
CREATE TYPE user_role AS ENUM ('PRODUCER', 'CONSUMER', 'BOTH');
CREATE TYPE energy_type AS ENUM ('GENERATION', 'CONSUMPTION');

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    wallet_balance  DECIMAL(18, 8) NOT NULL DEFAULT 0.00,
    price_per_kwh   DECIMAL(18, 8) NOT NULL DEFAULT 0.50,
    role            user_role   NOT NULL DEFAULT 'CONSUMER',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Energy Logs Table (will be converted to TimescaleDB hypertable in V2)
CREATE TABLE IF NOT EXISTS energy_logs (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    timestamp   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    meter_id    UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    kwh_value   DOUBLE PRECISION NOT NULL,
    type        energy_type NOT NULL,
    PRIMARY KEY (id, timestamp)
);

-- Transactions Table
CREATE TABLE IF NOT EXISTS transactions (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    buyer_id        UUID        NOT NULL REFERENCES users(id),
    seller_id       UUID        NOT NULL REFERENCES users(id),
    amount_kwh      DOUBLE PRECISION NOT NULL,
    price_paid      DECIMAL(18, 8) NOT NULL,
    co2_saved_kg    DOUBLE PRECISION NOT NULL,
    executed_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_energy_logs_meter_id ON energy_logs (meter_id);
CREATE INDEX idx_energy_logs_timestamp ON energy_logs (timestamp DESC);
CREATE INDEX idx_transactions_buyer_id ON transactions (buyer_id);
CREATE INDEX idx_transactions_seller_id ON transactions (seller_id);
