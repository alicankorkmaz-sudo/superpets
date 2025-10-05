# Supabase Migration Guide

## Overview

This guide walks you through migrating from Firebase Firestore to Supabase PostgreSQL to resolve the gRPC/IPv6 connectivity issues.

**What we're keeping:**
- ✅ Firebase Authentication (frontend already uses it)
- ✅ All existing API endpoints and contracts
- ✅ Credit system logic and atomic transactions

**What we're replacing:**
- ❌ Firestore (database only) → Supabase PostgreSQL
- ❌ Firebase Admin SDK (for database) → PostgreSQL JDBC + Exposed ORM

## Step 1: Create Supabase Project

1. Go to [supabase.com](https://supabase.com)
2. Sign in with GitHub
3. Click "New Project"
4. Fill in:
   - **Name**: `superpets` (or your preference)
   - **Database Password**: Generate a strong password (save this!)
   - **Region**: Choose closest to your users
   - **Pricing Plan**: Free tier is fine for development

5. Wait for project setup to complete (~2 minutes)

## Step 2: Set Up Database Schema

1. In your Supabase dashboard, go to **SQL Editor**
2. Click **New Query**
3. Copy the contents of `supabase_migration.sql` into the editor
4. Click **Run** to execute the migration
5. Verify tables were created in **Table Editor**

You should see:
- `users` table
- `credit_transactions` table
- `edit_history` table

## Step 3: Get Supabase Credentials

### ⚠️ CRITICAL: Use Transaction Pooler for Cloud Deployments

**For local development:**
- Direct connection (port 5432) works fine

**For cloud deployments (Render, Cloud Run, etc.):**
- ✅ **MUST use Transaction Pooler** (port 6543) - IPv4 compatible
- ❌ **DO NOT use Direct Connection** (port 5432) - IPv6 only, will fail on most cloud platforms

### Getting the Connection String

1. In Supabase dashboard, go to **Project Settings** → **Database**
2. Scroll down to **Connection String** section
3. Find the **Transaction mode** tab
4. Select **URI** format
5. Click **Copy** to get the full connection string

**Connection String Format (Transaction Pooler):**
```bash
# For cloud deployments (Render, Cloud Run, etc.) - USE THIS
SUPABASE_DB_URL=postgresql://postgres.[PROJECT-REF]:[PASSWORD]@aws-0-[REGION].pooler.supabase.com:6543/postgres

# Example for project 'zrivjktyzllaevduydai' in eu-central-1:
# postgresql://postgres.zrivjktyzllaevduydai:T8OzqpzjKHk3KZfZ@aws-1-eu-central-1.pooler.supabase.com:6543/postgres
```

**Connection String Format (Direct - Local Only):**
```bash
# For local development only
SUPABASE_DB_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
```

**Key Differences:**
- **Pooler**: `postgres.[PROJECT-REF]@aws-X-[REGION].pooler.supabase.com:6543`
- **Direct**: `postgres@db.[PROJECT-REF].supabase.co:5432`

**Important Notes:**
- Notice the username format changes: `postgres.[PROJECT-REF]` (pooler) vs `postgres` (direct)
- Host changes: `pooler.supabase.com` vs `db.supabase.co`
- Port changes: `6543` (pooler) vs `5432` (direct)
- Do NOT include braces `[]` - replace with actual values

## Step 4: Update Backend Dependencies

This is done automatically by the migration script. Dependencies added:
- PostgreSQL JDBC Driver
- Exposed ORM (core, DAO, JDBC)
- HikariCP (connection pooling)

## Step 5: Environment Variables

Add to your `.env` or deployment environment:

```bash
# Existing Firebase Auth (keep these)
FIREBASE_SERVICE_ACCOUNT_JSON={"type":"service_account",...}

# New Supabase Database
SUPABASE_DB_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres

# Existing API keys (keep these)
FAL_API_KEY=your_fal_key
STRIPE_SECRET_KEY=your_stripe_key
STRIPE_WEBHOOK_SECRET=your_webhook_secret
```

## Step 6: Migration Testing Checklist

After migration completes, test these endpoints:

### Authentication (should still work)
- [ ] POST `/nano-banana/edit` - with Firebase token
- [ ] GET `/user/profile` - returns user data

### User Management
- [ ] GET `/user/profile` - auto-creates new user with 5 credits
- [ ] GET `/user/credits` - returns credit balance
- [ ] POST `/user/credits/add` - adds credits with transaction log

### Transactions & History
- [ ] GET `/user/transactions` - shows transaction history (ordered DESC)
- [ ] GET `/user/edits` - shows edit history (ordered DESC)

### Image Editing (credit deduction)
- [ ] POST `/nano-banana/edit` - deducts credits, saves edit history
- [ ] POST `/nano-banana/upload-and-edit` - same as above with file upload

### Stripe Integration
- [ ] POST `/stripe/create-checkout-session` - creates checkout
- [ ] POST `/stripe/webhook` - adds credits on successful payment

### Edge Cases
- [ ] Insufficient credits → returns 402 Payment Required
- [ ] Concurrent credit deduction (test atomic transactions)
- [ ] Query limits (50 items max on history endpoints)

## Step 7: Data Migration (if needed)

If you have existing users in Firestore, you'll need to export and import:

### Export from Firestore
```bash
# In superpets-backend/, create a script:
./gradlew run --args="export-users"
```

### Import to Supabase
1. Use Supabase SQL Editor
2. Run INSERT statements from exported data
3. Verify data in Table Editor

**Note**: If you have no production users yet, skip this step.

## Step 8: Deploy to Cloud Platform

### ⚠️ CRITICAL: Use Transaction Pooler Connection String

When deploying to any cloud platform (Render, Google Cloud Run, Fly.io, etc.), you **MUST** use the Transaction Pooler connection string (port 6543), not the direct connection (port 5432).

### Render Deployment

1. Go to Render dashboard
2. Navigate to your service → **Environment**
3. Update `SUPABASE_DB_URL` to use **Transaction Pooler** connection:

```bash
# ✅ CORRECT (Transaction Pooler - port 6543)
SUPABASE_DB_URL=postgresql://postgres.zrivjktyzllaevduydai:T8OzqpzjKHk3KZfZ@aws-1-eu-central-1.pooler.supabase.com:6543/postgres

# ❌ WRONG (Direct connection - port 5432) - Will fail with "Network unreachable"
SUPABASE_DB_URL=postgresql://postgres:T8OzqpzjKHk3KZfZ@db.zrivjktyzllaevduydai.supabase.co:5432/postgres
```

4. Click **Save Changes**
5. Render will automatically redeploy with the new connection string

### Google Cloud Run Deployment

```bash
# Build and deploy
docker build -t gcr.io/[PROJECT-ID]/superpets-backend .
docker push gcr.io/[PROJECT-ID]/superpets-backend

# Deploy with Transaction Pooler connection (port 6543)
gcloud run deploy superpets-backend \
  --image gcr.io/[PROJECT-ID]/superpets-backend \
  --platform managed \
  --region us-central1 \
  --set-env-vars="SUPABASE_DB_URL=postgresql://postgres.[PROJECT-REF]:[PASSWORD]@aws-X-[REGION].pooler.supabase.com:6543/postgres" \
  --set-env-vars="SUPABASE_URL=https://[PROJECT-REF].supabase.co" \
  --set-env-vars="SUPABASE_JWT_SECRET=[YOUR-JWT-SECRET]" \
  --set-env-vars="FAL_API_KEY=..." \
  --set-env-vars="STRIPE_SECRET_KEY=..." \
  --set-env-vars="STRIPE_WEBHOOK_SECRET=..."
```

### Why Transaction Pooler is Required

- **Direct Connection (port 5432)**: IPv6 only, fails on most cloud platforms
- **Transaction Pooler (port 6543)**: IPv4 compatible, works everywhere
- Most cloud platforms (Render, Cloud Run, Fly.io) use IPv4-only networking
- Using the wrong connection will result in "Network unreachable" or "No route to host" errors

## Benefits After Migration

✅ **No more gRPC errors** - PostgreSQL uses standard TCP connections
✅ **Works everywhere** - No IPv6/IPv4 compatibility issues
✅ **Better querying** - SQL gives you more flexibility than Firestore
✅ **Cost effective** - Supabase free tier is generous
✅ **Real-time optional** - Supabase offers real-time subscriptions if needed
✅ **Better local dev** - Can run PostgreSQL locally easily

## Rollback Plan

If you need to rollback:
1. Keep Firebase dependencies in `build.gradle.kts`
2. Switch routing back to `FirestoreService`
3. Redeploy with Firestore config

The migration is designed to be reversible.

## Support

If you encounter issues:
1. Check Supabase logs: Dashboard → Database → Logs
2. Check application logs: `./gradlew run` output
3. Verify connection string is correct
4. Ensure RLS policies allow service role access
