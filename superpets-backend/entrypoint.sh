#!/bin/sh

# Write Firebase service account JSON from env var to file if it exists
if [ -n "$FIREBASE_SERVICE_ACCOUNT_JSON" ]; then
    echo "$FIREBASE_SERVICE_ACCOUNT_JSON" > /app/firebase-service-account.json
    echo "Firebase service account file created from environment variable"
fi

# Start the application with IPv4 enforcement for Firestore gRPC connectivity
# These flags MUST be set at JVM startup before any network classes are loaded
exec java \
  -Djava.net.preferIPv4Stack=true \
  -Djava.net.preferIPv4Addresses=true \
  -Dio.grpc.netty.shaded.io.netty.transport.noNative=true \
  -jar app.jar
