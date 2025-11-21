# Superpets Launch Checklist

## Phase 1: Critical Pre-Launch (Must Complete Before Going Live)

### Payment Integration
- [ ] Receipt/invoice display

### Pricing Strategy
- [ ] Set refund policy

### Security Hardening
- [ ] **CRITICAL**: Restrict CORS in backend (currently allows all hosts)
  - Update `application.conf` to only allow production domain
- [ ] Add rate limiting to prevent abuse
  - [ ] Implement per-user rate limits (e.g., 10 edits/minute)
  - [ ] Add IP-based rate limiting for auth endpoints
- [ ] Validate all user inputs
  - [ ] Image URL validation (check file size, format)
  - [ ] Prompt text sanitization (max length, blocked words)
- [ ] Secure environment variables
  - [ ] Use secrets manager for production (AWS Secrets Manager, Google Secret Manager)
  - [ ] Never commit `firebase-service-account.json` to git
  - [ ] Rotate API keys before launch
- [ ] Add request size limits (prevent DoS)
- [ ] Enable HTTPS only (no HTTP)
- [ ] Set up security headers (CSP, HSTS, X-Frame-Options)
- [ ] Review Firebase security rules for Firestore

### Legal & Compliance
- [ ] Consider business entity formation (LLC for liability protection)

### Testing & Quality Assurance
- [ ] Load testing

### Marketing & Launch Prep
- [ ] Set up social media accounts (Twitter, Instagram)
- [ ] Prepare launch announcement

### Feature Enhancements
- [ ] Image history gallery

### Business Growth
- [ ] SEO optimization
