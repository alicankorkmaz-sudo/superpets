# Railway Monorepo Configuration Guide

**Issue:** Railway triggers deployments even when only mobile/frontend files change, not backend files.

**Solution:** Configure Railway to only watch the `superpets-backend/` directory for changes.

---

## Configuration Steps (Via Railway Dashboard)

### Step 1: Access Your Railway Project

1. Open your browser and go to:
   https://railway.com/project/b7df09da-2741-413c-8474-4baab3059775

2. Navigate to your **superpets-backend** service

### Step 2: Update Service Settings

1. Click on your backend service (should be named something like "superpets-backend" or the main service)

2. Go to the **Settings** tab

3. Scroll down to the **"Service Settings"** or **"Deploys"** section

4. Look for these configuration options:

   **A. Root Directory**
   - Set to: `superpets-backend`
   - This tells Railway to build from the backend directory

   **B. Watch Paths (Most Important)**
   - Click "Add Watch Path"
   - Add: `superpets-backend/**`
   - This ensures Railway ONLY triggers builds when files in this directory change

### Step 3: Save and Test

1. Click **"Save"** or **"Update"**

2. Make a change to a file OUTSIDE the backend directory (e.g., update `superpets-mobile/MOBILE_STATUS.md`)

3. Commit and push

4. Verify that Railway does NOT trigger a deployment

5. Then make a change to a file INSIDE the backend directory (e.g., update `superpets-backend/README.md`)

6. Commit and push

7. Verify that Railway DOES trigger a deployment

---

## Alternative: Using Railway API (Advanced)

If you prefer using the API, here's how:

### Get Your Service ID

```bash
# Get your Railway API token
railway whoami

# List all services in the project
curl -X POST https://backboard.railway.app/graphql/v2 \
  -H "Authorization: Bearer $(railway token)" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query project($id: String!) { project(id: $id) { services { edges { node { id name } } } } }",
    "variables": {"id": "b7df09da-2741-413c-8474-4baab3059775"}
  }'
```

### Update Watch Paths

Once you have your service ID:

```bash
curl -X POST https://backboard.railway.app/graphql/v2 \
  -H "Authorization: Bearer $(railway token)" \
  -H "Content-Type: application/json" \
  -d '{
    "query": "mutation serviceUpdate($id: String!, $input: ServiceUpdateInput!) { serviceUpdate(id: $id, input: $input) { id } }",
    "variables": {
      "id": "YOUR_SERVICE_ID",
      "input": {
        "rootDirectory": "superpets-backend",
        "watchPaths": ["superpets-backend/**"]
      }
    }
  }'
```

---

## How Watch Paths Work

**Watch Paths** tell Railway which files to monitor for changes. When you push a commit:

1. Railway checks if any changed files match the watch paths
2. If YES → Trigger deployment
3. If NO → Skip deployment

**Example:**

With watch path `superpets-backend/**`:

| Changed Files | Deploy? | Reason |
|--------------|---------|--------|
| `superpets-backend/src/main/kotlin/Application.kt` | ✅ YES | Matches pattern |
| `superpets-backend/build.gradle.kts` | ✅ YES | Matches pattern |
| `superpets-backend/Dockerfile` | ✅ YES | Matches pattern |
| `superpets-mobile/MOBILE_STATUS.md` | ❌ NO | Doesn't match pattern |
| `superpets-web/src/pages/Home.tsx` | ❌ NO | Doesn't match pattern |
| `CLAUDE.md` (root) | ❌ NO | Doesn't match pattern |

---

## Verification

After configuring, you can verify the settings:

```bash
# From repo root
railway status

# Should show:
# Project: superpets-backend
# Environment: production
# Service: [your-service-name]
```

---

## Troubleshooting

### Issue: Watch Paths Not Working

**Possible Causes:**
1. Watch paths not saved properly in Railway dashboard
2. Root directory not set correctly
3. Service not properly linked

**Solution:**
1. Double-check the watch paths in Railway dashboard
2. Ensure root directory is set to `superpets-backend`
3. Re-link the service if needed: `railway link`

### Issue: Still Triggering on All Changes

**Check:**
1. Are you using automatic deployments from GitHub?
2. Is the correct branch configured?
3. Are watch paths using wildcards correctly?

**Expected Pattern:**
- `superpets-backend/**` (matches all files recursively)
- NOT `superpets-backend/*` (only matches direct children)

---

## Current Configuration

**Project:** superpets-backend
**Project ID:** b7df09da-2741-413c-8474-4baab3059775
**Environment:** production
**Dashboard:** https://railway.com/project/b7df09da-2741-413c-8474-4baab3059775

**Required Settings:**
- ✅ Root Directory: `superpets-backend`
- ✅ Watch Paths: `superpets-backend/**`
- ✅ Build Method: Dockerfile
- ✅ Dockerfile Path: `Dockerfile` (relative to root directory)

---

## Notes

- Railway's watch paths configuration is a **project-level** setting, not a file-based config
- The `railway.toml` file is useful for build commands and environment variables, but NOT for watch paths in monorepos
- Watch paths must be configured via the Railway dashboard or GraphQL API
- Changes to watch paths take effect immediately for future deployments

---

**Last Updated:** October 14, 2025
