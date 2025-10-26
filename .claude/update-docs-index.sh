#!/bin/bash

# Documentation Index Update Script
# Discovers all AI-generated markdown files in the superpets project
# and outputs them in a format ready to update session-init.md

echo "=== Superpets Documentation Discovery ==="
echo ""
echo "Scanning for markdown files (excluding dependencies)..."
echo ""

# Root level documentation
echo "### Root Directory"
find /Users/alican.korkmaz/Code/superpets \
  -maxdepth 1 \
  -name "*.md" \
  -not -name "README.md" \
  | sort

echo ""
echo "### Backend Documentation (superpets-backend/)"
find /Users/alican.korkmaz/Code/superpets/superpets-backend \
  -maxdepth 1 \
  -name "*.md" \
  | sort

echo ""
echo "### Mobile Documentation (superpets-mobile/)"
find /Users/alican.korkmaz/Code/superpets/superpets-mobile \
  -maxdepth 1 \
  -name "*.md" \
  | sort

echo ""
echo "### Frontend Documentation (superpets-web/)"
find /Users/alican.korkmaz/Code/superpets/superpets-web \
  -maxdepth 1 \
  -name "*.md" \
  | sort

echo ""
echo "=== Total Documentation Files ==="
total=$(find /Users/alican.korkmaz/Code/superpets \
  -name "*.md" \
  -not -path "*/node_modules/*" \
  -not -path "*/.gradle/*" \
  -not -path "*/build/*" \
  -not -path "*/.kotlin/*" \
  -not -path "*/iosApp/Pods/*" \
  | grep -v "/README.md" \
  | wc -l | tr -d ' ')
echo "Found $total documentation files"
echo ""
echo "Update .claude/session-init.md with any new files discovered above."
