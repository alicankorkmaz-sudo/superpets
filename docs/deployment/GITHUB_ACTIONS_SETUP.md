# GitHub Actions Setup for Automatic Frontend Deployment

This guide will help you configure automatic deployment to Firebase Hosting whenever you push to the `main` branch.

---

## Prerequisites

- GitHub repository: `alicankorkmaz-sudo/superpets`
- Firebase project: `superpets-ee0ab`
- Admin access to both GitHub and Firebase

---

## Step 1: Generate Firebase Service Account Key

1. **Go to Firebase Console:**
   - Visit: https://console.firebase.google.com/project/superpets-ee0ab/settings/serviceaccounts/adminsdk

2. **Generate new private key:**
   - Click "Generate new private key"
   - Confirm the download
   - A JSON file will be downloaded (e.g., `superpets-ee0ab-firebase-adminsdk-xxxxx.json`)

3. **Copy the JSON content:**
   - Open the downloaded JSON file
   - Copy the **entire contents** of the file
   - You'll need this in Step 2

---

## Step 2: Add GitHub Secrets

1. **Go to GitHub Repository Settings:**
   - Visit: https://github.com/alicankorkmaz-sudo/superpets/settings/secrets/actions

2. **Add FIREBASE_SERVICE_ACCOUNT secret:**
   - Click "New repository secret"
   - Name: `FIREBASE_SERVICE_ACCOUNT`
   - Value: Paste the **entire JSON content** from Step 1
   - Click "Add secret"

3. **Add VITE_STRIPE_PUBLISHABLE_KEY secret:**
   - Click "New repository secret"
   - Name: `VITE_STRIPE_PUBLISHABLE_KEY`
   - Value: `pk_test_51SEGggRVlNnZcpuImz9GGK0ItrObzZlpPG1zcZpaUghrH1WZdZ6FN0zUooV1LarZetr4d3eWs1QqJ7lCtHHXphCb00TfK18rVV`
   - Click "Add secret"

---

## Step 3: Verify GitHub Actions is Enabled

1. **Go to GitHub Actions settings:**
   - Visit: https://github.com/alicankorkmaz-sudo/superpets/settings/actions

2. **Check Actions permissions:**
   - Ensure "Allow all actions and reusable workflows" is selected
   - Or at minimum, allow GitHub Actions from verified creators

---

## Step 4: Test the Workflow

Once the secrets are added and the workflow file is committed:

1. **Push a change to `main` branch:**
   ```bash
   # Make any change to superpets-web/ directory
   echo "# Test change" >> superpets-web/README.md
   git add superpets-web/README.md
   git commit -m "Test automatic deployment"
   git push origin main
   ```

2. **Watch the deployment:**
   - Visit: https://github.com/alicankorkmaz-sudo/superpets/actions
   - You should see a workflow run titled "Deploy Frontend to Firebase Hosting"
   - Click on it to see the deployment progress

3. **Verify deployment:**
   - Once successful, visit https://superpets.fun
   - Check that your changes are live

---

## How It Works

### Workflow File Location
`.github/workflows/firebase-deploy.yml`

### Trigger Conditions
The workflow runs automatically when:
- You push to the `main` branch
- AND files in `superpets-web/` directory are modified
- OR the workflow file itself is modified

### Deployment Steps
1. Checkout repository code
2. Setup Node.js 20
3. Install dependencies (`npm ci`)
4. Create `.env.production` with secrets
5. Build production bundle (`npm run build`)
6. Deploy to Firebase Hosting (live channel)

### Environment Variables
The workflow creates `.env.production` with:
- `VITE_API_BASE_URL`: Points to Render backend
- `VITE_STRIPE_PUBLISHABLE_KEY`: From GitHub secrets

**Note:** `VITE_SUPABASE_URL` and `VITE_SUPABASE_ANON_KEY` are read from the committed `.env` file (they're the same for dev and production).

---

## Monitoring Deployments

### GitHub Actions Dashboard
- View all workflow runs: https://github.com/alicankorkmaz-sudo/superpets/actions
- See deployment logs, timing, and status

### Firebase Hosting Dashboard
- View deployment history: https://console.firebase.google.com/project/superpets-ee0ab/hosting/sites

---

## Troubleshooting

### Workflow Fails: "Error: HTTP Error: 403, The caller does not have permission"
**Solution:** Verify `FIREBASE_SERVICE_ACCOUNT` secret contains valid JSON and has correct permissions.

### Workflow Fails: "npm ERR! code ENOENT"
**Solution:** Verify `working-directory` and `cache-dependency-path` in workflow file are correct.

### Deployment Succeeds but Changes Not Visible
**Solution:**
- Check if Firebase Hosting cache is enabled
- Try hard refresh in browser (Cmd+Shift+R or Ctrl+Shift+F5)
- Verify deployment in Firebase Console

### Workflow Doesn't Trigger
**Solution:**
- Ensure changes are in `superpets-web/` directory
- Verify workflow file syntax is correct
- Check GitHub Actions is enabled for the repository

---

## Rollback a Deployment

If you need to rollback to a previous version:

1. **Via Firebase Console:**
   - Go to Firebase Hosting: https://console.firebase.google.com/project/superpets-ee0ab/hosting/sites
   - Find the previous successful deployment
   - Click the three dots → "Rollback"

2. **Via Git:**
   - Revert the commit and push to `main`
   - GitHub Actions will automatically deploy the previous version

---

## Disabling Automatic Deployment

To temporarily disable automatic deployments:

1. **Option 1: Disable the workflow**
   - Go to: https://github.com/alicankorkmaz-sudo/superpets/actions
   - Find "Deploy Frontend to Firebase Hosting"
   - Click the three dots → "Disable workflow"

2. **Option 2: Delete the workflow file**
   ```bash
   git rm .github/workflows/firebase-deploy.yml
   git commit -m "Disable automatic deployments"
   git push origin main
   ```

---

## Next Steps After Setup

Once automatic deployment is working:

1. ✅ Update `DEPLOYMENT_SUMMARY.md` to reflect CI/CD setup
2. ✅ Consider adding deployment status badge to README
3. ✅ Set up Slack/Discord notifications for deployment status (optional)
4. ✅ Consider adding a staging environment with preview channels

---

## Security Notes

- ✅ Firebase service account JSON is stored as GitHub encrypted secret
- ✅ Secrets are never exposed in workflow logs
- ✅ Only repository admins can view/modify secrets
- ⚠️ Stripe test key is safe to use in GitHub secrets, but switch to production key when ready

---

**Created:** October 5, 2025
**Status:** Ready to configure
