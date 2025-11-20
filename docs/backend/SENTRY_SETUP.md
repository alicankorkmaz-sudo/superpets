# Sentry Setup for Superpets Backend

## Environment Variables

Add these environment variables to your Railway service:

### Required:

```bash
SENTRY_DSN=https://fecdb6efce57b3b12addc0b24bbbd569@o4510156131008512.ingest.de.sentry.io/4510156138348624
```

### Optional (for local development):

```bash
SENTRY_ENVIRONMENT=development  # Use "production" on Railway
SENTRY_AUTH_TOKEN=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
```

## Railway Setup

1. Go to your Railway dashboard: https://railway.com/project/b7df09da-2741-413c-8474-4baab3059775
2. Click on **Environment** tab
3. Add new environment variable:
   - **Key:** `SENTRY_DSN`
   - **Value:** `https://fecdb6efce57b3b12addc0b24bbbd569@o4510156131008512.ingest.de.sentry.io/4510156138348624`
4. (Optional) Add:
   - **Key:** `SENTRY_ENVIRONMENT`
   - **Value:** `production`
5. (Optional) Add for build-time source uploads:
   - **Key:** `SENTRY_AUTH_TOKEN`
   - **Value:** `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`
6. Click **Save Changes**
7. Railway will automatically redeploy with Sentry enabled

## Local Development

For local testing, create a `.env` file in the backend root:

```bash
SENTRY_DSN=https://fecdb6efce57b3b12addc0b24bbbd569@o4510156131008512.ingest.de.sentry.io/4510156138348624
SENTRY_ENVIRONMENT=development
SENTRY_AUTH_TOKEN=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
```

Then run:
```bash
export $(cat .env | xargs)
./gradlew run
```

## What Gets Tracked

Sentry will automatically capture:

✅ **Errors:**
- Uncaught exceptions
- All WARN and ERROR level logs
- API endpoint failures
- Database connection errors
- Authentication failures

✅ **Performance:**
- API response times
- Database query performance
- Transaction tracing
- Request throughput

✅ **Context:**
- User IDs (from authenticated requests)
- Request URLs and parameters
- Stack traces
- Server environment info

## Testing Sentry

After deployment, test Sentry by triggering an error:

```bash
# This should create a Sentry event
curl -X GET https://api.superpets.fun/admin/stats
# Returns 401 - logged as warning in Sentry
```

Check your Sentry dashboard: https://alican-korkmaz.sentry.io/issues/

## Dashboard

View errors and performance: https://alican-korkmaz.sentry.io/projects/superpets-backend/
