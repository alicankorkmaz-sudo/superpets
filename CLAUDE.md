# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Superpets is a full-stack application for AI-powered image editing using Google's Nano Banana model via fal.ai. The project consists of:

- **Ktor Backend** (`Ktor Superpets/`): Kotlin-based REST API server
- **React Web Frontend** (`superpets-web/`): TypeScript + React + Vite application
- **Mobile** (`superpets-mobile/`): Currently empty placeholder

## Architecture

### Backend (Ktor)

The backend is organized into services, models, and config layers:

**Key Services:**
- `NanoBananaService`: Handles fal.ai API integration for image editing and file uploads
- `FirestoreService`: Manages user data, credit system, transactions, and edit history
- `FirebaseAuthService`: Handles Firebase authentication token verification
- `HeroService`: Manages hero data from `pets.json` and provides prompt building functionality

**Authentication Flow:**
- Firebase bearer token authentication (`firebase-auth`)
- Token verification via `FirebaseAuthService.verifyIdToken()`
- User ID extracted to `UserIdPrincipal` for route handlers

**Credit System:**
- New users auto-created with 10 free credits on first API access
- Pricing: **1 credit per image generated** (not per request)
  - Example: Generating 5 images costs 5 credits
  - Example: Generating 1 image costs 1 credit
- Atomic credit transactions using Firestore transactions
- Full transaction history tracked in subcollection
- Client-side and server-side credit validation before processing

**Main Routes:**
- `/heroes` - Get all available heroes (public, no auth required)
- `/nano-banana/edit` - Edit images from URLs (requires `hero_id`, supports 1-10 output images)
- `/nano-banana/upload-and-edit` - Upload and edit images (multipart/form-data, requires `hero_id`, supports 1-10 input files)
- `/user/profile` - Get user profile (auto-creates with 10 credits)
- `/user/credits` - Get credit balance
- `/user/transactions` - Get transaction history
- `/user/edits` - Get edit history
- `/user/credits/add` - Add credits (admin/webhook)

All authenticated routes require `Authorization: Bearer <firebase-token>` header.

**Hero-Based Prompt System:**
- Heroes are defined in `pets.json` with ID, name, identity, and 10 scene options
- Two categories: "classics" (10 heroes like Superman, Batman) and "uniques" (19 custom heroes)
- Edit endpoints **require** `hero_id` (custom prompts have been removed)
- **Multi-image variety**: When `num_images > 1`, each output gets a unique random scene
  - Prompts generated upfront before parallel API calls to ensure variety
  - Example: 4 images = 4 different scenes from the hero's 10 options
- Prompt format: "Transform the pet into {hero}. Keep the pet's identity like face, fur, and body proportions etc exactly the same. Add {identity}. Place them {scene}. Avoid distortions or altering the pet's natural features."
- Prompt building happens server-side before calling fal.ai API

**Parallel Processing:**
- Multiple output images processed in parallel for performance
- Each parallel request gets a unique prompt (different scene)
- All requests share the same input images
- Performance: 10 images in ~15 seconds (vs 150 seconds sequential)

### Frontend (React + TypeScript)

**Project Structure:**
- `src/lib/` - API client (`api.ts`), Firebase config, and TypeScript types
- `src/contexts/` - React contexts (CreditsContext for global credit state)
- `src/hooks/` - Custom hooks (`useAuth`, `useImageEdit`)
- `src/components/` - Organized by feature (Auth, Dashboard, Editor)
- `src/pages/` - Page components (EditorPage)

**Key Patterns:**
- Firebase authentication managed via `useAuth` hook
- Global credit state via `CreditsProvider` context
- API calls use centralized `api.ts` with automatic token injection
- Error handling for `UNAUTHORIZED` and `INSUFFICIENT_CREDITS` states
- **Client-side credit validation** before API calls
  - Button disabled when `credits < numImages`
  - Validation error shown immediately without API call
- **Hero selection required** - custom prompts have been removed from UI
- Dynamic credit cost display: "Generate Images (5 credits)"

**API Configuration:**
- Base URL: `http://localhost:8080` (hardcoded in `src/lib/api.ts`)
- Authentication: Bearer tokens from Firebase Auth
- Image generation limit: 1-10 images per request

## Development Commands

### Backend (Ktor)

Navigate to `Ktor Superpets/` directory:

```bash
# Run the development server (localhost:8080)
./gradlew run

# Run tests
./gradlew test

# Build the project
./gradlew build

# Build executable JAR with dependencies
./gradlew buildFatJar

# Build Docker image
./gradlew buildImage

# Publish Docker image locally
./gradlew publishImageToLocalRegistry

# Run using local Docker image
./gradlew runDocker
```

**Environment Configuration:**
- Requires `firebase-service-account.json` in project root
- FAL API key configured via `fal.apiKey` in application.conf or environment
- `pets.json` must be in project root (contains hero definitions)

**Test Interface:**
- Simple HTML interface available at `http://localhost:8080/index.html`
- Includes Firebase auth (login/register), API testing, and image upload with hero selection
- Hero selection UI with "Classic Heroes" and "Unique Heroes" tabs
- Users can either select a hero or enter a custom prompt (mutually exclusive)

### Frontend (React)

Navigate to `superpets-web/` directory:

```bash
# Install dependencies
npm install

# Run development server (with hot reload)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

**Tech Stack:**
- React 19 with TypeScript
- Vite for build tooling
- Tailwind CSS for styling
- Firebase SDK for authentication
- Lucide React for icons
- date-fns for date formatting

## Important Implementation Details

### Firebase Configuration
- Backend requires `firebase-service-account.json` for Firebase Admin SDK
- Frontend uses Firebase client SDK configured in `src/lib/firebase.ts`
- CORS configured in backend to allow all hosts (restrict in production)

### Credit System Flow
1. User authenticates via Firebase
2. First API call auto-creates user with 10 credits
3. Before image edit, credits are deducted atomically
4. Edit is performed only if deduction succeeds
5. Edit history saved to Firestore for user reference

### File Upload Flow
1. Client sends multipart form data to `/nano-banana/upload-and-edit`
2. Backend extracts files and metadata (including `prompt` or `hero_id`)
3. If `hero_id` provided, prompt is generated using `HeroService.buildPrompt()`
4. Files uploaded to fal.ai storage via `NanoBananaService.uploadFile()`
5. Returned URLs and final prompt used in `editImage()` request
6. Result and history saved to Firestore

### Hero System Files
**Backend:**
- `pets.json` - Hero data file in project root (10 classics + 19 uniques)
- `src/main/kotlin/models/HeroModels.kt` - Data models (Hero, HeroesData, HeroesResponse)
- `src/main/kotlin/services/HeroService.kt` - Hero loading and prompt building
  - `loadHeroesFromFile()` - Loads and parses `pets.json`
  - `getAllHeroes()` - Returns all heroes
  - `getHeroById(heroId)` - Finds specific hero
  - `Hero.buildPrompt()` - Extension function to generate random prompts

**Frontend (Test HTML):**
- `src/main/resources/static/index.html` - Updated with hero selection UI
  - Fetches heroes from `/heroes` endpoint
  - Displays in tabbed grid (classics/uniques)
  - Selection logic (hero OR custom prompt, mutually exclusive)
  - Sends `hero_id` in form data when hero selected

### Error Handling
- `401 Unauthorized` - Invalid/expired Firebase token
- `402 Payment Required` - Insufficient credits
- Credits checked before API calls, refunds not implemented
- All Firestore operations wrapped in try/catch with logging

## Launch Preparation

See `LAUNCH_CHECKLIST.md` in the project root for a comprehensive, prioritized checklist of tasks needed before going live. This includes:
- Payment integration (Stripe)
- Pricing strategy and credit packages
- Security hardening (CORS, rate limiting, input validation)
- Deployment infrastructure
- Legal requirements (ToS, Privacy Policy)
- Analytics and monitoring
- Mobile app planning (Compose Multiplatform)
- memorize