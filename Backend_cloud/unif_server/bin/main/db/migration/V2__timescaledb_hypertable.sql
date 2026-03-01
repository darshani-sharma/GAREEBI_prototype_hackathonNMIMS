-- V2: Convert energy_logs to TimescaleDB Hypertable for time-series optimization
-- TimescaleDB must be installed (timescale/timescaledb image used in docker-compose)

SELECT create_hypertable('energy_logs', 'timestamp', if_not_exists => TRUE);

-- Set chunk time interval to 1 day (appropriate for IoT energy data volume)
SELECT set_chunk_time_interval('energy_logs', INTERVAL '1 day');
