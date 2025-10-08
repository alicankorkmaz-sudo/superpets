-- Migration to add admin role support
-- Run this in your Supabase SQL editor

-- Add is_admin column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_admin BOOLEAN DEFAULT false;

-- Optional: Make your user an admin (replace with your actual user ID)
-- UPDATE users SET is_admin = true WHERE uid = 'your-user-id-here';
