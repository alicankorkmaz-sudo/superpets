## Project Overview

Superpets is a full-stack monorepo application for AI-powered pet image editing using Google's Nano Banana model via fal.ai. The project transforms pet photos into superhero versions.

**Monorepo Structure:**
- **Backend** (`superpets-backend/`): Ktor (Kotlin) REST API server
- **Frontend** (`superpets-web/`): React + TypeScript + Vite web application
- **Mobile** (`superpets-mobile/`): Compose Multiplatform (Android & iOS)

## Git Repository

- **GitHub:** `https://github.com/alicankorkmaz-sudo/superpets`

## Current Deployment Status

**Backend:** Deployed to Railway ✅
- Production URL: https://api.superpets.fun (custom subdomain)
- Railway Project: https://railway.com/project/b7df09da-2741-413c-8474-4baab3059775
- Database: Supabase PostgreSQL
- Authentication: Supabase Auth
- Deployment: Triggered by GitHub Actions from `main` and watch path `superpets-backend/**`

**Frontend:** Deployed to Firebase Hosting ✅
- Production URL: https://superpets.fun (custom domain)
- Firebase Hosting URL: https://superpets-ee0ab.web.app
- Connected to Railway backend via environment variables
- Deployment: Automatic via GitHub Actions (on push to `main`) and watch path `superpets-web/**`
- CI/CD: `.github/workflows/firebase-deploy.yml`

**Domain:**
- Custom domain: **superpets.fun**

## Architecture

### Backend (Ktor - Kotlin)

**Directory:** `superpets-backend/`

**Deployment Configuration:**
- Environment variables needed:
  - `SUPABASE_DB_URL`: PostgreSQL connection string (transaction pooler for IPv4 compatibility)
  - `SUPABASE_URL`: Supabase project URL
  - `SUPABASE_JWT_SECRET`: JWT secret for token verification
  - `FAL_API_KEY`: fal.ai API key
  - `STRIPE_SECRET_KEY`: Stripe secret key
  - `STRIPE_WEBHOOK_SECRET`: Stripe webhook secret

### Frontend (React + TypeScript)

**Directory:** `superpets-web/`

**API Configuration:**
- Base URL managed via `VITE_API_BASE_URL` environment variable

### Mobile (Compose Multiplatform)

**Directory:** `superpets-mobile/`
