# Sentry Setup for Superpets Frontend

## What's Integrated

Sentry is now fully integrated into the React frontend with:

✅ **Error Tracking**
- Automatic capture of all unhandled errors
- API error tracking with context
- React component errors via error boundary
- Source maps for production debugging

✅ **Performance Monitoring**
- Browser tracing integration
- 100% transaction sampling
- API response time tracking
- Page load performance

✅ **Session Replay**
- 10% of normal sessions recorded
- 100% of error sessions recorded
- Privacy-focused (no sensitive data masked)

✅ **User Context**
- Automatic user ID and email tracking
- Links errors to specific users
- User activity tracking

## Environment Variables

### Development (.env)
Already configured:
```bash
VITE_SENTRY_DSN=https://97451e74e65149567bfd79fd401b88b7@o4510156131008512.ingest.de.sentry.io/4510156145098832
VITE_SENTRY_ENVIRONMENT=development
```

### Production (.env.production)
Already configured:
```bash
VITE_SENTRY_DSN=https://97451e74e65149567bfd79fd401b88b7@o4510156131008512.ingest.de.sentry.io/4510156145098832
VITE_SENTRY_ENVIRONMENT=production
```

## GitHub Actions Configuration

The Firebase deployment workflow already uses `.env.production`, so Sentry will be automatically enabled in production.

No additional configuration needed! 🎉

## What Gets Tracked

### Errors
- ✅ Unhandled JavaScript errors
- ✅ Promise rejections
- ✅ React component errors
- ✅ API call failures (4xx, 5xx)
- ✅ Network errors
- ✅ Authentication failures

### Performance
- ✅ Page load times
- ✅ API response times
- ✅ Component render times
- ✅ Navigation timing
- ✅ Resource loading

### User Context
- ✅ User ID (Supabase user ID)
- ✅ Email address
- ✅ Browser info
- ✅ Device info
- ✅ Page URL
- ✅ User actions before error

## Testing Sentry

### Option 1: Trigger a Test Error (Development)
Add this button temporarily to any component:

```tsx
<button onClick={() => {
  throw new Error('Sentry test error!');
}}>
  Test Sentry
</button>
```

### Option 2: Test in Browser Console
```javascript
// In browser console
throw new Error("Testing Sentry error tracking");
```

### Option 3: Test API Error
Try accessing an endpoint without auth:
```javascript
fetch('https://superpets-backend-pipp.onrender.com/admin/stats')
```

## Sentry Dashboard

View errors and performance:
- **Project URL**: https://alican-korkmaz.sentry.io/projects/superpets-web/
- **Issues**: https://alican-korkmaz.sentry.io/issues/
- **Performance**: https://alican-korkmaz.sentry.io/performance/

## What's Excluded from Tracking

To avoid noise, these errors are **not** sent to Sentry:
- `UNAUTHORIZED` errors (expected behavior)
- `INSUFFICIENT_CREDITS` errors (expected behavior)

## Source Maps

Vite automatically generates source maps in production builds, which Sentry uses to provide readable stack traces.

No additional configuration needed!

## Session Replay

Session replay helps debug issues by showing exactly what the user did before an error:

- **Normal sessions**: 10% recorded (to save quota)
- **Error sessions**: 100% recorded (to debug issues)
- **Privacy**: Text and media are not masked (no sensitive data in app)

You can adjust these settings in `src/main.tsx`:
```typescript
replaysSessionSampleRate: 0.1,  // 10% of normal sessions
replaysOnErrorSampleRate: 1.0,  // 100% of error sessions
```

## Quota Management

**Free Tier Limits:**
- Errors: 5,000/month
- Performance: 10,000 transactions/month
- Replays: 50 sessions/month

If you exceed these, oldest data is dropped first. Consider upgrading if needed.

## Best Practices

1. **Check Sentry Daily** - Review errors in dashboard
2. **Set up Alerts** - Get notified of critical errors via email/Slack
3. **Release Tracking** - Errors are tagged with release version (0.0.1)
4. **User Feedback** - Users are automatically tagged in errors
5. **Performance Budgets** - Monitor slow API calls

## Support

- **Sentry Docs**: https://docs.sentry.io/platforms/javascript/guides/react/
- **Dashboard**: https://alican-korkmaz.sentry.io/

## Next Deploy

On your next push to `main`, GitHub Actions will:
1. Build the frontend with Sentry enabled
2. Deploy to Firebase with production configuration
3. Errors and performance data will flow to Sentry automatically

🎉 **You're all set!**
