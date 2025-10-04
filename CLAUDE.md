# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Superpets is a full-stack monorepo application for AI-powered pet image editing using Google's Nano Banana model via fal.ai. The project transforms pet photos into superhero versions.

**Monorepo Structure:**
- **Backend** (`superpets-backend/`): Ktor (Kotlin) REST API server
- **Frontend** (`superpets-web/`): React + TypeScript + Vite web application
- **Mobile** (`superpets-mobile/`): Empty placeholder for future development

## Current Deployment Status

**Backend:** Not yet deployed (preparing for Google Cloud Run)
- Fly.io was attempted but had gRPC/Firestore connectivity issues (IPv6 incompatibility)
- Dockerfile and entrypoint.sh ready for containerized deployment
- Secrets stored as environment variables (not in code)

**Frontend:** Running locally only
- API base URL: `http://localhost:8080` (needs update for production)
- Will be deployed to Vercel/Netlify after backend is live

## Architecture

### Backend (Ktor - Kotlin)

**Directory:** `superpets-backend/`

**Key Services:**
- `NanoBananaService`: fal.ai API integration for image editing and file uploads
- `FirestoreService`: User data, credit system, transactions, edit history
- `FirebaseAuthService`: Firebase authentication token verification
- `HeroService`: Hero data management and prompt generation
- `StripeService`: Payment processing (Stripe integration)

**Authentication Flow:**
- Firebase bearer token authentication (`firebase-auth`)
- Token verification via `FirebaseAuthService.verifyIdToken()`
- User ID extracted to `UserIdPrincipal` for route handlers

**Credit System:**
- New users auto-created with **10 free credits** on first API access
- Pricing: **1 credit per image generated** (not per request)
  - Example: 5 images = 5 credits, 1 image = 1 credit
- Atomic credit transactions using Firestore transactions
- Full transaction history tracked in subcollection
- Client-side and server-side credit validation before processing

**Main API Routes:**
- `/heroes` - Get all available heroes (public, no auth)
- `/nano-banana/edit` - Edit images from URLs (requires `hero_id`, 1-10 outputs)
- `/nano-banana/upload-and-edit` - Upload and edit (multipart/form-data, requires `hero_id`, 1-10 files)
- `/user/profile` - Get user profile (auto-creates with 10 credits)
- `/user/credits` - Get credit balance
- `/user/transactions` - Get transaction history
- `/user/edits` - Get edit history
- `/user/credits/add` - Add credits (admin/webhook)
- `/stripe/webhook` - Stripe payment webhook
- `/stripe/create-checkout-session` - Create Stripe checkout

All authenticated routes require `Authorization: Bearer <firebase-token>` header.

**Hero-Based Prompt System:**
- Heroes defined in `pets.json` (10 classics + 19 unique heroes)
- Categories: "classics" (Superman, Batman, etc.) and "uniques" (custom characters)
- Edit endpoints **require** `hero_id` (custom prompts removed)
- **Multi-image variety**: When `num_images > 1`, each gets a unique random scene
  - Prompts generated upfront before parallel API calls
  - Example: 4 images = 4 different scenes from hero's 10 options
- Prompt format: "Transform the pet into {hero}. Keep the pet's identity like face, fur, and body proportions etc exactly the same. Add {identity}. Place them {scene}. Avoid distortions or altering the pet's natural features."
- Prompt building happens server-side before calling fal.ai

**Parallel Processing:**
- Multiple output images processed in parallel for performance
- Each parallel request gets unique prompt (different scene)
- All requests share the same input images
- Performance: 10 images in ~15 seconds (vs 150 seconds sequential)

**Deployment Configuration:**
- `Dockerfile`: Multi-stage build (Gradle + OpenJDK 17)
- `entrypoint.sh`: Handles Firebase service account JSON from env var
- Environment variables needed:
  - `FIREBASE_SERVICE_ACCOUNT_JSON`: Full JSON content
  - `FAL_API_KEY`: fal.ai API key
  - `STRIPE_SECRET_KEY`: Stripe secret key
  - `STRIPE_WEBHOOK_SECRET`: Stripe webhook secret

### Frontend (React + TypeScript)

**Directory:** `superpets-web/`

**Project Structure:**
- `src/lib/` - API client (`api.ts`), Firebase config, TypeScript types
- `src/contexts/` - React contexts (CreditsContext for global credit state)
- `src/hooks/` - Custom hooks (`useAuth`, `useImageEdit`)
- `src/components/` - Feature-organized components (Auth, Dashboard, Editor)
- `src/pages/` - Page components (EditorPage, PricingPage)

**Key Patterns:**
- Firebase authentication via `useAuth` hook
- Global credit state via `CreditsProvider` context
- Centralized API calls in `api.ts` with automatic token injection
- Error handling for `UNAUTHORIZED` and `INSUFFICIENT_CREDITS` states
- **Client-side credit validation** before API calls
  - Button disabled when `credits < numImages`
  - Validation error shown immediately without API call
- **Hero selection required** - custom prompts removed from UI
- Dynamic credit cost display: "Generate Images (5 credits)"

**API Configuration:**
- Base URL: `http://localhost:8080` (in `src/lib/api.ts` - needs update for production)
- Authentication: Bearer tokens from Firebase Auth
- Image generation limit: 1-10 images per request

**Tech Stack:**
- React 19 with TypeScript
- Vite for build tooling
- Tailwind CSS for styling
- Firebase SDK for authentication
- Lucide React for icons
- date-fns for date formatting

## Development Commands

### Backend (Ktor)

Navigate to `superpets-backend/`:

```bash
# Run dev server (localhost:8080)
./gradlew run

# Run tests
./gradlew test

# Build JAR with dependencies
./gradlew shadowJar

# Build Docker image locally
docker build -t superpets-backend .

# Run Docker container locally
docker run -p 8080:8080 \
  -e FIREBASE_SERVICE_ACCOUNT_JSON="$(cat firebase-service-account.json)" \
  -e FAL_API_KEY="your_key" \
  -e STRIPE_SECRET_KEY="your_key" \
  -e STRIPE_WEBHOOK_SECRET="your_secret" \
  superpets-backend
```

**Environment Configuration:**
- Requires `firebase-service-account.json` in project root (DO NOT COMMIT)
- `pets.json` must be in project root (contains hero definitions)
- See `.env.example` for required environment variables

**Test Interface:**
- HTML test interface at `http://localhost:8080/index.html`
- Includes Firebase auth, API testing, image upload with hero selection
- Hero selection UI with "Classic Heroes" and "Unique Heroes" tabs

### Frontend (React)

Navigate to `superpets-web/`:

```bash
# Install dependencies
npm install

# Run dev server (with hot reload)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## Important Implementation Details

### Firebase Configuration
- Backend: `firebase-service-account.json` for Firebase Admin SDK
- Frontend: Firebase client SDK in `src/lib/firebase.ts`
- **Security:** CORS set to `anyHost()` - **MUST restrict for production**

### Credit System Flow
1. User authenticates via Firebase
2. First API call auto-creates user with 10 credits
3. Before image edit, credits are deducted atomically
4. Edit is performed only if deduction succeeds
5. Edit history saved to Firestore for user reference

### File Upload Flow
1. Client sends multipart form data to `/nano-banana/upload-and-edit`
2. Backend extracts files and metadata (including `hero_id`)
3. If `hero_id` provided, prompt generated using `HeroService.buildPrompt()`
4. Files uploaded to fal.ai storage via `NanoBananaService.uploadFile()`
5. Returned URLs and final prompt used in `editImage()` request
6. Result and history saved to Firestore

### Stripe Payment Integration
- See `STRIPE_SETUP.md` for detailed implementation guide
- Checkout session creation via `/stripe/create-checkout-session`
- Webhook handling at `/stripe/webhook` for payment confirmation
- Credit packages configurable via Stripe product metadata

### Hero System Files
**Backend:**
- `pets.json` - Hero data file (10 classics + 19 uniques)
- `src/main/kotlin/models/HeroModels.kt` - Data models
- `src/main/kotlin/services/HeroService.kt` - Hero loading and prompt building

**Frontend (Test HTML):**
- `src/main/resources/static/index.html` - Hero selection UI
  - Fetches heroes from `/heroes` endpoint
  - Displays in tabbed grid (classics/uniques)
  - Sends `hero_id` in form data

### Error Handling
- `401 Unauthorized` - Invalid/expired Firebase token
- `402 Payment Required` - Insufficient credits
- Credits checked before API calls, refunds not implemented
- All Firestore operations wrapped in try/catch with logging

## Git Repository

- **GitHub:** `https://github.com/alicankorkmaz-sudo/superpets`
- **Branch:** `main`
- **Structure:** Monorepo with separate subdirectories for backend, web, and mobile

## Next Steps / TODO

See `PROJECT_STATE.md` for current progress and next steps.

Key priorities:
1. **Deploy backend to Google Cloud Run**
2. **Update frontend API URL for production**
3. **Deploy frontend to Vercel/Netlify**
4. **Implement CORS restrictions**
5. **Complete Stripe payment integration**
6. **Add rate limiting and input validation**

See `LAUNCH_CHECKLIST.md` for comprehensive pre-launch checklist.

## Notes

- **memorize** - This instruction ensures AI assistants remember key project details
- All secrets managed via environment variables (never committed to git)
- Firebase service account JSON stored as environment variable for deployment
- Hero prompt system is server-side only (clients cannot send custom prompts)
