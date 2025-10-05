# Supabase Migration Complete Guide

## Overview

This migration replaces Firebase Firestore with Supabase PostgreSQL for the database layer and Firebase Auth with Supabase Auth for authentication. This resolves gRPC/IPv6 connectivity issues that occurred with Firestore.

## What Changed

### Backend Changes

**Replaced:**
- ❌ Firebase Firestore → ✅ Supabase PostgreSQL
- ❌ Firebase Admin SDK (database) → ✅ Exposed ORM + PostgreSQL JDBC
- ❌ FirestoreService → ✅ SupabaseService
- ❌ FirebaseAuthService → ✅ SupabaseAuthService
- ❌ Firebase Auth tokens → ✅ Supabase JWT tokens

**New Files:**
- `src/main/kotlin/database/DatabaseFactory.kt` - Database connection management
- `src/main/kotlin/database/Tables.kt` - Exposed ORM table definitions
- `src/main/kotlin/services/SupabaseService.kt` - Database service (replaces FirestoreService)
- `src/main/kotlin/services/SupabaseAuthService.kt` - Auth service (replaces FirebaseAuthService)

**Modified Files:**
- `src/main/kotlin/Application.kt` - Now initializes Supabase DB, uses SupabaseAuthService
- `src/main/kotlin/Routing.kt` - All routes now use SupabaseService instead of FirestoreService
- `build.gradle.kts` - Added PostgreSQL, Exposed ORM, JWT dependencies
- `Dockerfile` - Simplified (no Firebase service account handling)
- `entrypoint.sh` - Removed (no longer needed)

### Frontend Changes

**Replaced:**
- ❌ Firebase SDK → ✅ Supabase JS client
- ❌ Firebase Auth → ✅ Supabase Auth

**Modified Files:**
- `package.json` - Removed `firebase`, added `@supabase/supabase-js`
- `src/lib/firebase.ts` → `src/lib/supabase.ts` - New Supabase client initialization
- `src/hooks/useAuth.ts` - Now uses Supabase Auth methods
- `src/lib/api.ts` - Now gets tokens from Supabase session
- `.env.example` - Added Supabase configuration

## Step-by-Step Setup

### 1. Create Supabase Project

1. Go to [supabase.com](https://supabase.com) and sign in
2. Click "New Project"
3. Fill in project details:
   - **Name**: `superpets`
   - **Database Password**: Generate and save a strong password
   - **Region**: Choose closest to your users (e.g., `us-east-1`)
4. Wait for project creation (~2 minutes)

### 2. Run Database Migration

1. In Supabase Dashboard, go to **SQL Editor**
2. Click **New Query**
3. Copy contents from `superpets-backend/supabase_migration.sql`
4. Click **Run** to execute
5. Verify tables in **Table Editor**:
   - ✅ `users`
   - ✅ `credit_transactions`
   - ✅ `edit_history`

### 3. Get Supabase Credentials

#### For Backend (Database Connection)

1. Go to **Project Settings** → **Database**
2. Find **Connection String** (URI format)
3. Copy the connection string
4. Replace `[YOUR-PASSWORD]` with your database password

**Save this:**
```
SUPABASE_DB_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
```

#### For Backend (JWT Verification)

1. Go to **Project Settings** → **API**
2. Copy **JWT Secret** (under "JWT Settings")

**Save this:**
```
SUPABASE_JWT_SECRET=your-jwt-secret-here
```

#### For Frontend (Client Configuration)

1. Go to **Project Settings** → **API**
2. Copy **Project URL**
3. Copy **anon public** key

**Save these:**
```
VITE_SUPABASE_URL=https://[PROJECT-REF].supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key-here
```

### 4. Configure Backend Environment

Create or update your backend environment variables:

```bash
# Supabase Database
SUPABASE_DB_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres
SUPABASE_URL=https://[PROJECT-REF].supabase.co
SUPABASE_JWT_SECRET=your-jwt-secret-here

# Existing API Keys (keep these)
FAL_API_KEY=your_fal_key
STRIPE_SECRET_KEY=your_stripe_key
STRIPE_WEBHOOK_SECRET=your_webhook_secret
```

**Note:** You no longer need `FIREBASE_SERVICE_ACCOUNT_JSON`

### 5. Configure Frontend Environment

Update `superpets-web/.env`:

```bash
# Supabase Configuration
VITE_SUPABASE_URL=https://[PROJECT-REF].supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key-here

# API Configuration
VITE_API_BASE_URL=http://localhost:8080

# Stripe Configuration
VITE_STRIPE_PUBLISHABLE_KEY=pk_test_your_key_here
```

### 6. Install Dependencies

#### Backend
```bash
cd superpets-backend
./gradlew build
```

#### Frontend
```bash
cd superpets-web
npm install
```

### 7. Enable Email Authentication in Supabase

1. Go to **Authentication** → **Providers** in Supabase Dashboard
2. Enable **Email** provider
3. Configure email templates if needed (optional)
4. For development, you may want to disable email confirmation:
   - Go to **Authentication** → **Settings**
   - Disable "Enable email confirmations"

### 8. Test Locally

#### Start Backend
```bash
cd superpets-backend
./gradlew run
```

#### Start Frontend
```bash
cd superpets-web
npm run dev
```

#### Test Flow
1. Visit `http://localhost:5173`
2. Sign up with a new email/password
3. Check Supabase Dashboard → **Authentication** → **Users** to see new user
4. Verify you get 5 free credits
5. Try uploading and editing an image
6. Check **Table Editor** to verify:
   - User created in `users` table
   - Transaction logged in `credit_transactions`
   - Edit saved in `edit_history`

### 9. Deploy Backend

#### Google Cloud Run Example

```bash
# Build Docker image
docker build -t gcr.io/[PROJECT-ID]/superpets-backend .

# Push to Google Container Registry
docker push gcr.io/[PROJECT-ID]/superpets-backend

# Deploy to Cloud Run
gcloud run deploy superpets-backend \
  --image gcr.io/[PROJECT-ID]/superpets-backend \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars="SUPABASE_DB_URL=postgresql://postgres:[PASSWORD]@db.[PROJECT-REF].supabase.co:5432/postgres" \
  --set-env-vars="SUPABASE_URL=https://[PROJECT-REF].supabase.co" \
  --set-env-vars="SUPABASE_JWT_SECRET=your-jwt-secret" \
  --set-env-vars="FAL_API_KEY=..." \
  --set-env-vars="STRIPE_SECRET_KEY=..." \
  --set-env-vars="STRIPE_WEBHOOK_SECRET=..."
```

#### Render Example

1. Go to Render Dashboard
2. Create New → Web Service
3. Connect your GitHub repository
4. Configure:
   - **Build Command**: `./gradlew shadowJar`
   - **Start Command**: `java -jar build/libs/app.jar`
   - **Environment Variables**: Add all Supabase and API keys
5. Deploy

### 10. Deploy Frontend

Update `VITE_API_BASE_URL` in production `.env` to your deployed backend URL.

#### Vercel
```bash
cd superpets-web
vercel --prod
```

#### Netlify
```bash
cd superpets-web
npm run build
netlify deploy --prod --dir=dist
```

## Migration Benefits

✅ **No more gRPC errors** - PostgreSQL uses standard TCP/IP
✅ **Works everywhere** - No IPv6/IPv4 compatibility issues
✅ **Better querying** - Full SQL capabilities vs Firestore limitations
✅ **Cost effective** - Supabase free tier is very generous
✅ **Real-time optional** - Supabase offers real-time subscriptions
✅ **Better local dev** - Easy to run PostgreSQL locally or use Supabase
✅ **Unified auth** - Same provider for both database and authentication

## Schema Comparison

### Firestore (Old)
```
users/{userId}
  - uid: string
  - email: string
  - credits: number
  - createdAt: timestamp

users/{userId}/transactions/{transactionId}
  - userId: string
  - amount: number
  - type: enum
  - description: string
  - timestamp: timestamp

users/{userId}/edits/{editId}
  - userId: string
  - prompt: string
  - inputImages: array
  - outputImages: array
  - creditsCost: number
  - timestamp: timestamp
```

### Supabase PostgreSQL (New)
```sql
users
  - uid: VARCHAR(128) PRIMARY KEY
  - email: VARCHAR(255)
  - credits: BIGINT
  - created_at: BIGINT

credit_transactions
  - id: UUID PRIMARY KEY
  - user_id: VARCHAR(128) REFERENCES users(uid)
  - amount: BIGINT
  - type: VARCHAR(50)
  - description: TEXT
  - timestamp: BIGINT

edit_history
  - id: UUID PRIMARY KEY
  - user_id: VARCHAR(128) REFERENCES users(uid)
  - prompt: TEXT
  - input_images: TEXT[]
  - output_images: TEXT[]
  - credits_cost: BIGINT
  - timestamp: BIGINT
```

## Authentication Flow Changes

### Old (Firebase)
1. User signs in with Firebase Auth (frontend)
2. Frontend gets Firebase ID token
3. Backend verifies token with Firebase Admin SDK
4. Token contains `uid`, `email`

### New (Supabase)
1. User signs in with Supabase Auth (frontend)
2. Frontend gets Supabase JWT access token
3. Backend verifies JWT signature with Supabase JWT secret
4. Token contains `sub` (user ID), `email`, `role`

**Key Difference:** Backend no longer needs to call external service to verify tokens - JWT verification is done locally with the secret.

## API Contract (Unchanged)

All API endpoints remain the same:
- `GET /heroes` - Public endpoint
- `GET /user/profile` - Get user profile
- `GET /user/credits` - Get credits
- `POST /user/credits/add` - Add credits
- `GET /user/transactions` - Get transaction history
- `GET /user/edits` - Get edit history
- `POST /nano-banana/edit` - Edit image from URL
- `POST /nano-banana/upload-and-edit` - Upload and edit
- `POST /stripe/create-checkout-session` - Create Stripe checkout
- `POST /stripe/webhook` - Stripe webhook

**Authentication:** Still uses `Authorization: Bearer <token>` header

## Troubleshooting

### Backend Issues

**Error: "SUPABASE_DB_URL environment variable is not set"**
- Make sure you've set the environment variable
- Verify the connection string format is correct

**Error: Connection refused / timeout**
- Check Supabase project is running
- Verify your IP is allowed (Supabase allows all by default)
- Check connection string password is correct

**Error: JWT verification failed**
- Verify `SUPABASE_JWT_SECRET` matches your Supabase project
- Check token is being sent in Authorization header

### Frontend Issues

**Error: "Missing Supabase environment variables"**
- Create `.env` file based on `.env.example`
- Verify `VITE_SUPABASE_URL` and `VITE_SUPABASE_ANON_KEY` are set

**Users can't sign up/in**
- Check Supabase Dashboard → Authentication → Providers
- Verify Email provider is enabled
- For dev, consider disabling email confirmation

**API calls failing with 401**
- Check if user is signed in
- Verify token is being retrieved from session
- Check backend is using correct JWT secret

## Rollback Plan

If you need to rollback to Firebase:

1. Keep Firebase dependencies in `build.gradle.kts` (currently still there)
2. Revert `Application.kt` to use `FirebaseConfig` and `FirebaseAuthService`
3. Revert `Routing.kt` to use `FirestoreService`
4. Revert frontend to use Firebase SDK
5. Restore `FIREBASE_SERVICE_ACCOUNT_JSON` environment variable
6. Redeploy

**Note:** All Supabase code is additive - you can keep both systems running in parallel during transition.

## Support

For issues:
- **Supabase:** Check dashboard logs, documentation at docs.supabase.com
- **Backend:** Check application logs from `./gradlew run` or deployment platform
- **Frontend:** Check browser console for errors
- **Database:** Use Supabase SQL Editor to query tables directly

## Next Steps

After successful migration:
1. Remove Firebase dependencies from `build.gradle.kts` (optional)
2. Delete `firebase-service-account.json` (if exists locally)
3. Remove Firebase config files from frontend
4. Update `CORS` in `Application.kt` from `anyHost()` to specific domains
5. Set up Supabase Row Level Security (RLS) policies for production
6. Configure Supabase backups and monitoring
7. Consider enabling Supabase real-time subscriptions for live updates
