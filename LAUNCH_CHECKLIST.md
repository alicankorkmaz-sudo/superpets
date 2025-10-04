# Superpets Launch Checklist

## Phase 1: Critical Pre-Launch (Must Complete Before Going Live)

### Payment Integration
- [ ] Choose payment provider (Stripe recommended for credits/subscriptions)
- [ ] Implement Stripe integration in backend
  - [ ] Create webhook endpoint for payment events
  - [ ] Add credits to user account on successful payment
  - [ ] Handle payment failures and disputes
- [ ] Create payment UI in web frontend
  - [ ] Credit purchase page with pricing tiers
  - [ ] Payment confirmation flow
  - [ ] Receipt/invoice display
- [ ] Test payment flow end-to-end (use Stripe test mode)
- [ ] Set up production Stripe account with real bank details

### Pricing Strategy
- [ ] Research competitor pricing (AI image editing tools)
- [ ] Define credit packages
  - Suggested: 10 credits = $5, 50 = $20, 100 = $35, 500 = $150
  - Free tier: 10 credits on signup (already implemented)
- [ ] Calculate cost per edit (fal.ai API costs)
- [ ] Ensure 50%+ profit margin per transaction
- [ ] Consider subscription option (e.g., $9.99/month for 100 credits)
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
  - Ensure users can only read/write their own data

### Deployment Infrastructure
- [ ] Choose hosting platform
  - Backend: Railway, Render, Fly.io, or GCP Cloud Run
  - Frontend: Vercel, Netlify, or Cloudflare Pages
- [ ] Set up production database (Firestore already used, verify project setup)
- [ ] Configure production Firebase project (separate from dev)
- [ ] Set up CI/CD pipeline
  - [ ] Automated backend deployment on push to main
  - [ ] Automated frontend deployment on push to main
  - [ ] Run tests before deployment
- [ ] Configure custom domain and SSL
- [ ] Set up monitoring and logging
  - [ ] Error tracking (Sentry, LogRocket)
  - [ ] Application logs (structured logging)
  - [ ] Uptime monitoring (UptimeRobot, Better Uptime)
- [ ] Set up backups for Firestore data
- [ ] Create staging environment for testing

### Legal & Compliance
- [ ] Create Terms of Service
  - No refunds policy / credit expiration
  - Acceptable use policy (no illegal content)
  - Liability limitations
- [ ] Create Privacy Policy
  - Data collection disclosure (Firebase, Firestore)
  - GDPR compliance if serving EU users
  - Cookie policy
- [ ] Add legal pages to website footer
- [ ] Consider business entity formation (LLC for liability protection)
- [ ] Research sales tax requirements (if applicable)

## Phase 2: Launch Ready (Complete Before Public Announcement)

### User Experience
- [ ] Add credit purchase CTA throughout app
  - Show credits remaining prominently
  - "Out of credits" modal with purchase button
- [ ] Improve onboarding flow
  - Welcome screen explaining credit system
  - Tutorial for first-time users
- [ ] Add loading states and progress indicators
- [ ] Implement proper error messages (user-friendly)
- [ ] Add FAQ/Help section
  - How credits work
  - What Nano Banana does
  - Pricing info
  - Contact support

### Analytics & Business Intelligence
- [ ] Set up analytics (Google Analytics 4 or Mixpanel)
  - Track user signups
  - Track credit purchases
  - Track image edits
  - Track conversion funnel
- [ ] Set up admin dashboard
  - View total revenue
  - View active users
  - View credit usage patterns
  - Monitor fal.ai costs
- [ ] Create alerts for critical metrics
  - Payment failures
  - API errors
  - Low conversion rate

### Testing & Quality Assurance
- [ ] Load testing
  - Test with 100+ concurrent users
  - Identify performance bottlenecks
- [ ] Security testing
  - Run OWASP ZAP or similar
  - Penetration testing (if budget allows)
- [ ] Cross-browser testing (Chrome, Safari, Firefox, Edge)
- [ ] Mobile web testing (responsive design)
- [ ] Test payment flow with real money (small amount)
- [ ] Test all error scenarios
  - Insufficient credits
  - API failures
  - Network errors

### Marketing & Launch Prep
- [ ] Create landing page (can be simple)
  - Hero section with demo
  - Pricing section
  - Social proof placeholder
- [ ] Set up social media accounts (Twitter, Instagram)
- [ ] Prepare launch announcement
- [ ] Set up customer support channel
  - Support email
  - Or live chat (Intercom, Crisp)
- [ ] Create email collection for waitlist/updates
- [ ] Plan launch on Product Hunt / Reddit / HackerNews

## Phase 3: Post-MVP Enhancements (Can Iterate After Launch)

### Mobile App (Compose Multiplatform)
- [ ] Set up Compose Multiplatform project
- [ ] Implement authentication flow
- [ ] Port image editor UI
- [ ] Add payment integration (in-app purchases)
- [ ] Submit to App Store and Play Store
- [ ] Consider mobile-specific features (camera integration)

### Feature Enhancements
- [ ] Batch editing (multiple images at once)
- [ ] Save favorite prompts
- [ ] Image history gallery
- [ ] Share edited images to social media
- [ ] Advanced editing options (more model parameters)
- [ ] Referral program (give credits for referrals)

### Performance & Scalability
- [ ] Implement CDN for static assets
- [ ] Add caching layer (Redis) for frequently accessed data
- [ ] Optimize database queries
- [ ] Implement image compression/optimization
- [ ] Consider edge functions for faster response times

### Business Growth
- [ ] A/B test pricing tiers
- [ ] Add subscription plans
- [ ] Enterprise/API access tier
- [ ] Affiliate program
- [ ] Content marketing (blog posts, tutorials)
- [ ] SEO optimization
- [ ] Paid advertising campaigns

## Immediate Next Steps (Recommended Order)

1. **Pricing Strategy** (1 day) - Define what you'll charge
2. **Security Hardening** (2-3 days) - Fix CORS, add rate limiting, input validation
3. **Payment Integration** (3-5 days) - Stripe backend + frontend
4. **Deployment** (2-3 days) - Get backend and frontend on production hosting
5. **Legal Pages** (1 day) - Basic ToS and Privacy Policy (can use templates)
6. **Testing** (2 days) - Full flow testing with real payments
7. **Analytics** (1 day) - Basic tracking setup
8. **Launch** (1 day) - Soft launch to small audience

**Estimated MVP Timeline: 2-3 weeks to first paying customer**

## Cost Estimates

### Fixed Costs (Monthly)
- Hosting (Backend + Frontend): $20-50
- Firebase (likely free tier initially): $0-25
- Monitoring/Logging: $0-20
- Domain: $1-2/month

### Variable Costs
- fal.ai API costs per edit: ~$0.01-0.05 (verify actual cost)
- Payment processing: 2.9% + $0.30 per transaction (Stripe)

### Revenue Projection
- Target: 100 paying users/month @ avg $10 = $1,000/month
- Costs: ~$100 infrastructure + ~$50 API = $150
- Net: ~$800/month (after fees)

## Notes
- Focus on Phase 1 items - they're required to legally accept money
- Mobile app is Phase 3 - web MVP can generate revenue first
- Don't over-engineer - launch fast, iterate based on user feedback
- Keep free tier (10 credits) to reduce friction and enable viral growth
