#!/bin/bash

# Sentry Backend Test Script
# Tests error tracking on the Superpets backend

echo "🔍 Testing Sentry Backend Integration"
echo "======================================"
echo ""

BACKEND_URL="https://superpets-backend-pipp.onrender.com"

echo "Backend URL: $BACKEND_URL"
echo ""

echo "📊 Test Results:"
echo "----------------"

# Test 1: Unauthorized access (should create 401 error in Sentry)
echo ""
echo "Test 1: Testing 401 Unauthorized Error..."
echo "Endpoint: GET /admin/stats (without auth)"
response=$(curl -s -w "\n%{http_code}" -X GET "$BACKEND_URL/admin/stats")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" == "401" ]; then
    echo "✅ Got 401 Unauthorized (expected)"
    echo "   This should appear in Sentry as an authentication error"
else
    echo "❌ Unexpected status code: $http_code"
    echo "   Response: $body"
fi

# Test 2: Invalid endpoint (404 error)
echo ""
echo "Test 2: Testing 404 Not Found Error..."
echo "Endpoint: GET /invalid-endpoint-test-$(date +%s)"
response=$(curl -s -w "\n%{http_code}" -X GET "$BACKEND_URL/invalid-endpoint-test-$(date +%s)")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" == "404" ]; then
    echo "✅ Got 404 Not Found (expected)"
    echo "   This may appear in Sentry depending on configuration"
else
    echo "❌ Unexpected status code: $http_code"
fi

# Test 3: Public endpoint that should work (to verify backend is running)
echo ""
echo "Test 3: Testing valid endpoint (health check)..."
echo "Endpoint: GET /heroes"
response=$(curl -s -w "\n%{http_code}" -X GET "$BACKEND_URL/heroes")
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" == "200" ]; then
    echo "✅ Got 200 OK - Backend is running"
    heroes_count=$(echo "$body" | grep -o '"id"' | wc -l | tr -d ' ')
    echo "   Found $heroes_count heroes in response"
else
    echo "❌ Backend may not be running properly"
    echo "   Status code: $http_code"
    echo "   Response: $body"
fi

# Test 4: Invalid request body (should trigger validation error)
echo ""
echo "Test 4: Testing 400 Bad Request (invalid data)..."
echo "Endpoint: POST /nano-banana/edit (with invalid data)"
response=$(curl -s -w "\n%{http_code}" -X POST "$BACKEND_URL/nano-banana/edit" \
    -H "Content-Type: application/json" \
    -d '{"invalid": "data"}')
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" == "401" ] || [ "$http_code" == "400" ]; then
    echo "✅ Got error response (expected - no auth or bad request)"
    echo "   Status: $http_code"
else
    echo "⚠️  Unexpected status code: $http_code"
fi

# Summary
echo ""
echo "======================================"
echo "📊 Summary"
echo "======================================"
echo ""
echo "Backend is accessible at: $BACKEND_URL"
echo ""
echo "Now check your Sentry dashboards:"
echo ""
echo "🔗 Backend Issues:"
echo "   https://alican-korkmaz.sentry.io/issues/?project=4510156138348624"
echo ""
echo "🔗 Backend Performance:"
echo "   https://alican-korkmaz.sentry.io/performance/?project=4510156138348624"
echo ""
echo "⚠️  Note: If you don't see errors in Sentry, verify that:"
echo "   1. SENTRY_DSN is configured on Render"
echo "   2. Backend has been redeployed after adding SENTRY_DSN"
echo "   3. Sentry initialization logs appear in Render logs"
echo ""
echo "To check Render logs:"
echo "   https://dashboard.render.com/web/srv-ctdhqcdsvqrc73et16q0/logs"
echo ""
