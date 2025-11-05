# Session Initialization - AI Documentation Index

This file is automatically referenced at the start of each Claude Code session to ensure awareness of all AI-generated documentation.

## Documentation Discovery System

**Last Updated:** 2025-11-05

### Quick Reference: Key Documentation Files

When starting a new session, Claude Code should be aware of these critical documentation files:

#### 🎯 Project Core (Root Level)
- **CLAUDE.md** - Main project instructions and guidelines (ALWAYS READ FIRST)
- **PROJECT_STATE.md** - Current status, completed items, and priorities
- **DEPLOYMENT_SUMMARY.md** - Production deployment status and live URLs
- **DOCUMENTATION_SYSTEM.md** - Documentation organization and guidelines
- **LAUNCH_CHECKLIST.md** - Pre-launch checklist

#### 🔐 Authentication & Security (Root Level)
- **GOOGLE_OAUTH_SETUP.md** - Google Sign-In configuration
- **APPLE_OAUTH_SETUP.md** - Apple Sign-In configuration
- **EMAIL_CONFIRMATION_SETUP.md** - Email confirmation setup
- **MOBILE_EMAIL_CONFIRMATION_SETUP.md** - Mobile-specific email confirmation

#### 🗄️ Database & Backend (Root Level + Backend)
- **SUPABASE_MIGRATION.md** - Firebase to Supabase migration guide
- **superpets-backend/SUPABASE_MIGRATION_GUIDE.md** - Backend-specific migration
- **superpets-backend/SENTRY_SETUP.md** - Backend error tracking

#### 💳 Payments & Deployment (Root Level)
- **STRIPE_SETUP.md** - Stripe payment integration
- **RAILWAY_MONOREPO_SETUP.md** - Railway deployment config
- **GITHUB_ACTIONS_SETUP.md** - CI/CD pipeline setup
- **SENTRY_DEPLOYMENT_GUIDE.md** - Error tracking and monitoring

#### 📱 Mobile App (superpets-mobile/)
- **MOBILE_STATUS.md** - Current mobile app status
- **MOBILE_ROADMAP.md** - Development roadmap
- **MOBILE_TODO.md** - Development todo list
- **INFRASTRUCTURE_SETUP.md** - Core infrastructure docs
- **API_CLIENT_CONFIGURATION.md** - HTTP client setup
- **DESIGN_TOKENS.md** - Design system tokens
- **DESIGN_SYSTEM_USAGE.md** - Design system guide
- **COMPONENT_LIBRARY.md** - Reusable components reference
- **ASSETS_GUIDE.md** - Visual assets inventory
- **STITCH_DESIGN_BRIEF.md** - UI design specifications
- **QUICK_START.md** - Mobile quick start guide
- **TESTING.md** - Testing guide

#### 🌐 Frontend (superpets-web/)
- **superpets-web/SENTRY_SETUP.md** - Frontend error tracking

---

## Complete Documentation Registry

### Root Directory (14 files)
```
/Users/alican.korkmaz/Code/superpets/
├── APPLE_OAUTH_SETUP.md
├── CLAUDE.md ⭐ (Main project instructions)
├── DEPLOYMENT_SUMMARY.md
├── DOCUMENTATION_SYSTEM.md
├── EMAIL_CONFIRMATION_SETUP.md
├── GITHUB_ACTIONS_SETUP.md
├── GOOGLE_OAUTH_SETUP.md
├── LAUNCH_CHECKLIST.md
├── MOBILE_EMAIL_CONFIRMATION_SETUP.md
├── PROJECT_STATE.md ⭐ (Current status)
├── RAILWAY_MONOREPO_SETUP.md
├── SENTRY_DEPLOYMENT_GUIDE.md
├── STRIPE_SETUP.md
└── SUPABASE_MIGRATION.md
```

### Backend Directory (3 files)
```
/Users/alican.korkmaz/Code/superpets/superpets-backend/
├── README.md
├── SENTRY_SETUP.md
└── SUPABASE_MIGRATION_GUIDE.md
```

### Mobile Directory (16 files)
```
/Users/alican.korkmaz/Code/superpets/superpets-mobile/
├── API_CLIENT_CONFIGURATION.md
├── APP_ICON_TODO.md
├── ASSETS_GUIDE.md
├── ASSETS_README.md
├── CLEAN_STARTING_POINT.md
├── COMPONENT_LIBRARY.md
├── DESIGN_SYSTEM_USAGE.md
├── DESIGN_TOKENS.md
├── INFRASTRUCTURE_SETUP.md
├── MOBILE_ROADMAP.md
├── MOBILE_STATUS.md ⭐ (Mobile current status)
├── MOBILE_TODO.md
├── QUICK_START.md
├── README.md
├── STITCH_DESIGN_BRIEF.md
└── TESTING.md
```

### Frontend Directory (2 files)
```
/Users/alican.korkmaz/Code/superpets/superpets-web/
├── README.md
└── SENTRY_SETUP.md
```

---

## Usage Instructions for Claude Code

### At Session Start
When beginning a new session, Claude Code should:

1. **Always read `CLAUDE.md` first** - Contains core project instructions
2. **Check `PROJECT_STATE.md`** - Understand current progress and priorities
3. **Reference this file** (`session-init.md`) - Know what documentation exists

### When Working on Specific Features
Based on the task, read relevant documentation:

- **Authentication work?** → Read OAuth and email confirmation guides
- **Backend/API work?** → Read Supabase migration and backend docs
- **Mobile development?** → Read mobile-specific docs (status, roadmap, design system)
- **Frontend work?** → Read frontend Sentry setup
- **Deployment issues?** → Read Railway, GitHub Actions, and deployment summary
- **Payment features?** → Read Stripe setup guide
- **Error tracking?** → Read Sentry deployment guide

### Documentation Discovery Command
To refresh this index or discover new documentation:
```bash
# Find all markdown files (excluding node_modules and standard files)
find /Users/alican.korkmaz/Code/superpets \
  -name "*.md" \
  -not -path "*/node_modules/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/build/*" \
  -not -name "CHANGELOG.md" \
  -not -name "LICENSE.md" \
  | grep -E "(SETUP|GUIDE|MIGRATION|DEPLOYMENT|TODO|STATUS|ROADMAP|CHECKLIST|CLAUDE|CONFIG)" \
  | sort
```

---

## Maintenance Notes

**When adding new documentation:**
1. Update this `session-init.md` file with the new file path
2. Add to the appropriate category (Core, Auth, Database, etc.)
3. Mark with ⭐ if it's critical for most sessions

**When removing documentation:**
1. Remove from this index
2. Document why it was removed in PROJECT_STATE.md

**Recommended file naming convention:**
- Use UPPERCASE for setup/config guides: `FEATURE_SETUP.md`
- Use descriptive names: `GOOGLE_OAUTH_SETUP.md` not `OAUTH.md`
- Keep in root for cross-cutting concerns
- Keep in subdirectories for component-specific docs

---

## Total Documentation Count
**36 AI-generated documentation files** across the monorepo (as of 2025-11-05)

