-- =========================================
-- Job Portal Database Schema
-- Target: PostgreSQL
-- =========================================

-- Create database (run manually if needed):
-- CREATE DATABASE jobportal;

CREATE TABLE IF NOT EXISTS job_openings (
    id              BIGSERIAL       PRIMARY KEY,
    title           VARCHAR(255)    NOT NULL,
    description     TEXT,
    location        VARCHAR(255),
    department      VARCHAR(255),
    employment_type VARCHAR(50)     NOT NULL DEFAULT 'FULL_TIME',
    posted_date     DATE            NOT NULL DEFAULT CURRENT_DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'OPEN',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS candidates (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    email       VARCHAR(255)    NOT NULL UNIQUE,
    resume      TEXT,
    phone       VARCHAR(50),
    skills      VARCHAR(1000),
    created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS applications (
    id              BIGSERIAL       PRIMARY KEY,
    candidate_id    BIGINT          NOT NULL REFERENCES candidates(id) ON DELETE CASCADE,
    job_opening_id  BIGINT          NOT NULL REFERENCES job_openings(id) ON DELETE CASCADE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'APPLIED',
    applied_date    DATE            NOT NULL DEFAULT CURRENT_DATE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (candidate_id, job_opening_id)
);

-- Indexes for common queries
CREATE INDEX IF NOT EXISTS idx_applications_candidate ON applications(candidate_id);
CREATE INDEX IF NOT EXISTS idx_applications_job       ON applications(job_opening_id);
CREATE INDEX IF NOT EXISTS idx_job_openings_status    ON job_openings(status);
