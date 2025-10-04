#!/bin/sh

# Write Firebase service account JSON from env var to file if it exists
if [ -n "$FIREBASE_SERVICE_ACCOUNT_JSON" ]; then
    echo "$FIREBASE_SERVICE_ACCOUNT_JSON" > /app/firebase-service-account.json
    echo "Firebase service account file created from environment variable"
fi

# Start the application
exec java -jar app.jar
