-- Supabase Migration: Replace Firestore with PostgreSQL
-- Run this SQL in your Supabase SQL Editor

-- Users table (replaces Firestore 'users' collection)
CREATE TABLE users (
    uid VARCHAR(128) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    credits BIGINT NOT NULL DEFAULT 0,
    created_at BIGINT NOT NULL
);

-- Credit transactions table (replaces Firestore 'users/{userId}/transactions' subcollection)
CREATE TABLE credit_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(128) NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    amount BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('PURCHASE', 'DEDUCTION', 'REFUND', 'BONUS')),
    description TEXT NOT NULL,
    timestamp BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for credit_transactions
CREATE INDEX idx_transactions_user_id ON credit_transactions(user_id);
CREATE INDEX idx_transactions_timestamp ON credit_transactions(timestamp DESC);
CREATE INDEX idx_transactions_user_timestamp ON credit_transactions(user_id, timestamp DESC);

-- Edit history table (replaces Firestore 'users/{userId}/edits' subcollection)
CREATE TABLE edit_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(128) NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    prompt TEXT NOT NULL,
    input_images TEXT[] NOT NULL,
    output_images TEXT[] NOT NULL,
    credits_cost BIGINT NOT NULL,
    timestamp BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes for edit_history
CREATE INDEX idx_edits_user_id ON edit_history(user_id);
CREATE INDEX idx_edits_timestamp ON edit_history(timestamp DESC);
CREATE INDEX idx_edits_user_timestamp ON edit_history(user_id, timestamp DESC);

-- Enable Row Level Security (RLS) - recommended for production
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE credit_transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE edit_history ENABLE ROW LEVEL SECURITY;

-- RLS Policies (allow service role to access all data, can add user policies later)
CREATE POLICY "Service role has full access to users" ON users
    FOR ALL USING (true);

CREATE POLICY "Service role has full access to credit_transactions" ON credit_transactions
    FOR ALL USING (true);

CREATE POLICY "Service role has full access to edit_history" ON edit_history
    FOR ALL USING (true);
