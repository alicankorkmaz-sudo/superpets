# Sentry Deployment Guide

## ✅ What's Already Done

### Backend (Kotlin/Ktor)
- ✅ Sentry SDK installed
- ✅ Error tracking configured
- ✅ Performance monitoring enabled
- ✅ Logback integration (WARN/ERROR logs → Sentry)
- ✅ Code pushed to GitHub

### Frontend (React)
- ✅ Sentry SDK installed
- ✅ Error tracking configured
- ✅ Performance monitoring enabled
- ✅ Session replay configured
- ✅ User context tracking
- ✅ API error tracking
- ✅ Code pushed to GitHub
- ✅ Environment variables configured

## 🔧 What You Need to Do

### Step 1: Configure Backend on Render (REQUIRED)

1. **Go to Render Dashboard:**
   - https://dashboard.render.com/web/srv-d3gqch9r0fns73brgue0

2. **Click on "Environment" tab**

3. **Add new environment variable:**
   - **Key:** `SENTRY_DSN`
   - **Value:** `https://fecdb6efce57b3b12addc0b24bbbd569@o4510156131008512.ingest.de.sentry.io/4510156138348624`

4. **Optional - Add environment name:**
   - **Key:** `SENTRY_ENVIRONMENT`
   - **Value:** `production`

5. **Optional - Add auth token for source uploads:**
   - **Key:** `SENTRY_AUTH_TOKEN`
   - **Value:** `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`

6. **Click "Save Changes"**

7. **Render will automatically redeploy** (~2-5 minutes)

### Step 2: Verify Deployment

#### Frontend (Automatic)
The frontend is already deploying via GitHub Actions. Check the workflow:
- https://github.com/alicankorkmaz-sudo/superpets/actions

#### Backend
After configuring Render environment variables, it will auto-deploy from GitHub.

## 🧪 Testing Sentry

### Test Backend Errors

1. **Trigger a test error:**
```bash
# This should create a 401 error in Sentry
curl -X GET https://superpets-backend-pipp.onrender.com/admin/stats
```

2. **Check Sentry Dashboard:**
   - https://alican-korkmaz.sentry.io/issues/?project=4510156138348624

### Test Frontend Errors

1. **Visit your app:** https://superpets.fun

2. **Open browser console and run:**
```javascript
throw new Error("Testing Sentry frontend tracking");
```

3. **Check Sentry Dashboard:**
   - https://alican-korkmaz.sentry.io/issues/?project=4510156145098832

## 📊 Sentry Dashboards

### Backend
- **Project:** superpets-backend
- **Issues:** https://alican-korkmaz.sentry.io/issues/?project=4510156138348624
- **Performance:** https://alican-korkmaz.sentry.io/performance/?project=4510156138348624

### Frontend
- **Project:** superpets-web
- **Issues:** https://alican-korkmaz.sentry.io/issues/?project=4510156145098832
- **Performance:** https://alican-korkmaz.sentry.io/performance/?project=4510156145098832
- **Replays:** https://alican-korkmaz.sentry.io/replays/?project=4510156145098832

## 🎯 What Sentry Will Track

### Backend Errors
- ✅ Uncaught exceptions
- ✅ Database errors
- ✅ API failures
- ✅ Authentication issues
- ✅ All WARN and ERROR logs

### Frontend Errors
- ✅ JavaScript errors
- ✅ Promise rejections
- ✅ React component errors
- ✅ API call failures
- ✅ Network errors

### Performance
- ✅ API response times
- ✅ Database query times
- ✅ Page load times
- ✅ Component render times

### User Context
- ✅ User ID and email
- ✅ Browser/device info
- ✅ URL and navigation
- ✅ Actions before error

## 📈 Monitoring Best Practices

1. **Check Sentry Daily**
   - Review new errors: https://alican-korkmaz.sentry.io/issues/

2. **Set Up Alerts**
   - Get email notifications for critical errors
   - Settings: https://alican-korkmaz.sentry.io/settings/alerts/

3. **Review Performance**
   - Monitor slow API calls
   - Check database query times
   - Optimize bottlenecks

4. **Session Replay**
   - Watch user sessions that had errors
   - Understand user behavior before crash

## 🚀 Next Steps

1. **Add Render environment variable** (SENTRY_DSN)
2. **Wait for Render to redeploy** (~5 minutes)
3. **Test error tracking** (curl command above)
4. **Verify in Sentry dashboard**
5. **Set up email alerts** (optional)

## 📝 Summary

After you add the `SENTRY_DSN` to Render:

- ✅ Backend errors → Sentry (backend project)
- ✅ Frontend errors → Sentry (web project)
- ✅ Performance metrics tracked
- ✅ User context included
- ✅ Session replays available

**Total setup time:** ~5 minutes

**You're almost done!** Just add the environment variable to Render and you'll have full error tracking and monitoring! 🎉
