#!/bin/sh

# Write Firebase service account JSON from env var to file if it exists
if [ -n "$FIREBASE_SERVICE_ACCOUNT_JSON" ]; then
    echo "$FIREBASE_SERVICE_ACCOUNT_JSON" > /app/firebase-service-account.json
    echo "Firebase service account file created from environment variable"
fi

# Start the application with gRPC optimization flags for containers
exec java \
  -Djava.net.preferIPv4Stack=true \
  -Dio.grpc.netty.shaded.io.netty.transport.noNative=true \
  -Dio.netty.transport.noNative=true \
  -Dcom.google.api.client.should_use_proxy=false \
  -jar app.jar
