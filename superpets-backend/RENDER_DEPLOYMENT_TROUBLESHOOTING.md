# Render Deployment Troubleshooting

## Current Issue: Database Connection Timeout

### Problem
Backend deployed to Render is failing with database connection errors:
- HTTP 400 Bad Request on all endpoints
- Curl requests timeout after 60+ seconds
- Application logs show "Network is unreachable" or connection timeout errors

### Root Cause
**Using wrong Supabase connection string format**

Render (like most cloud platforms) uses IPv4-only networking. Supabase's direct connection (port 5432) is IPv6-only and incompatible with Render.

### Solution

Update the `SUPABASE_DB_URL` environment variable in Render to use the **Transaction Pooler** connection string.

## Step-by-Step Fix

### 1. Get the Correct Connection String

In your Supabase dashboard:
1. Go to **Project Settings** → **Database**
2. Scroll to **Connection String** section
3. Click on **Transaction** tab (NOT "Direct connection")
4. Select **URI** format
5. Copy the connection string

**Format:**
```
postgresql://postgres.<project-ref>:<password>@aws-X-<region>.pooler.supabase.com:6543/postgres
```

**Example for project `zrivjktyzllaevduydai`:**
```
postgresql://postgres.zrivjktyzllaevduydai:T8OzqpzjKHk3KZfZ@aws-1-eu-central-1.pooler.supabase.com:6543/postgres
```

### 2. Update Render Environment Variable

1. Go to [Render Dashboard](https://dashboard.render.com)
2. Select your `superpets-backend` service
3. Click **Environment** tab
4. Find `SUPABASE_DB_URL` variable
5. Click **Edit**
6. Replace the value with the Transaction Pooler connection string (from step 1)
7. Click **Save Changes**

### 3. Verify Auto-Deploy

After saving:
- Render automatically triggers a new deployment
- Wait 2-3 minutes for deployment to complete
- Check deployment logs for success

### 4. Test the Deployment

```bash
# Test health endpoint
curl -I https://superpets-backend.onrender.com/heroes

# Expected response:
HTTP/2 200
content-type: application/json

# Test authenticated endpoint (requires Firebase token)
curl https://superpets-backend.onrender.com/user/profile \
  -H "Authorization: Bearer YOUR_FIREBASE_TOKEN"
```

## How to Identify the Issue

### ❌ Wrong Connection (Direct - Port 5432)
```bash
# Format: postgres@db.PROJECT.supabase.co:5432
postgresql://postgres:PASSWORD@db.zrivjktyzllaevduydai.supabase.co:5432/postgres
```

**Symptoms:**
- Deployment logs: "Network is unreachable" or "No route to host"
- Application never starts successfully
- Curl requests timeout
- HTTP 400 or 502 errors

### ✅ Correct Connection (Transaction Pooler - Port 6543)
```bash
# Format: postgres.PROJECT@aws-X-REGION.pooler.supabase.com:6543
postgresql://postgres.zrivjktyzllaevduydai:PASSWORD@aws-1-eu-central-1.pooler.supabase.com:6543/postgres
```

**Symptoms:**
- Deployment logs: "Successfully connected to Supabase database"
- Application starts and responds to requests
- Curl requests return HTTP 200
- API endpoints work correctly

## Quick Checklist

- [ ] Using Transaction Pooler connection string (port **6543**)
- [ ] Username format is `postgres.<project-ref>` (NOT just `postgres`)
- [ ] Host contains `pooler.supabase.com` (NOT `db.supabase.co`)
- [ ] No brackets `[]` in the connection string (use actual values)
- [ ] Password is correct (same as Supabase project password)
- [ ] All other environment variables are set:
  - `SUPABASE_URL`
  - `SUPABASE_JWT_SECRET`
  - `FAL_API_KEY`
  - `STRIPE_SECRET_KEY`
  - `STRIPE_WEBHOOK_SECRET`

## Debugging Commands

### Check Render Service Status
```bash
# Install Render CLI (if not already installed)
brew install render

# Login to Render
render login

# List services (requires workspace setup)
render services list -o json
```

### Check Deployment Logs
1. Go to Render Dashboard
2. Select your service
3. Click **Logs** tab
4. Look for:
   - "Connecting to Supabase database..."
   - "Successfully connected to Supabase database"
   - Or errors: "Network is unreachable", "Connection timeout"

### Test Locally with Pooler Connection
```bash
cd superpets-backend

# Update .env to use Transaction Pooler connection
export SUPABASE_DB_URL="postgresql://postgres.zrivjktyzllaevduydai:PASSWORD@aws-1-eu-central-1.pooler.supabase.com:6543/postgres"

# Run locally
./gradlew run

# If successful, you should see:
# "Successfully connected to Supabase database"
# "Application started in X.X seconds"
# "Responding at http://0.0.0.0:8080"
```

## Common Mistakes

1. **Using Direct Connection for Cloud Deployment**
   - Direct connection (port 5432) is IPv6-only
   - Only works for local development
   - Will fail on Render, Cloud Run, Fly.io, etc.

2. **Incorrect Username Format**
   - Transaction Pooler uses: `postgres.<project-ref>`
   - Direct connection uses: `postgres`
   - Don't mix them up!

3. **Including Placeholder Brackets**
   - ❌ `postgresql://postgres.[PROJECT-REF]:...`
   - ✅ `postgresql://postgres.zrivjktyzllaevduydai:...`

4. **Wrong Host**
   - Transaction Pooler: `aws-X-REGION.pooler.supabase.com`
   - Direct: `db.PROJECT-REF.supabase.co`

## Need More Help?

1. Check Supabase connection string generator:
   - Dashboard → Project Settings → Database → Connection String
   - Make sure **Transaction** tab is selected

2. Check Render logs:
   - Dashboard → Your Service → Logs

3. Check backend code:
   - `DatabaseFactory.kt` handles connection parsing
   - Logs connection attempts and errors

4. Verify Supabase project status:
   - Dashboard → Project → Status should be "Healthy"

## References

- [Supabase Connection Strings](https://supabase.com/docs/guides/database/connecting-to-postgres)
- [Render Environment Variables](https://render.com/docs/environment-variables)
- [SUPABASE_MIGRATION_GUIDE.md](./SUPABASE_MIGRATION_GUIDE.md)
