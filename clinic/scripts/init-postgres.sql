-- PostgreSQL initialization script for Clinic IPS
-- This script runs when the PostgreSQL container starts for the first time

-- Create database if it doesn't exist
-- (This is handled by POSTGRES_DB environment variable)

-- Set timezone
SET timezone = 'America/Bogota';

-- Create custom types if needed
-- (Add any PostgreSQL-specific types here)

-- Create indexes for better performance
-- These will be created automatically by Hibernate, but we can add custom ones

-- Enable necessary extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Set default configuration for the database
ALTER DATABASE clinic_db SET timezone = 'America/Bogota';

-- Create a function to automatically update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Log initialization
DO $$
BEGIN
    RAISE NOTICE 'PostgreSQL database initialized for Clinic IPS';
    RAISE NOTICE 'Database: clinic_db';
    RAISE NOTICE 'Timezone: America/Bogota';
END $$;