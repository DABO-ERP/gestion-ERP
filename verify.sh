#!/bin/bash

# Verification script for Gestion ERP

set -e

echo "🔍 Running Gestion ERP Verification..."

# Check Gradle wrapper
echo "✓ Checking Gradle..."
./gradlew --version

# Compile project
echo "✓ Compiling project..."
./gradlew clean compileJava

# Run tests
echo "✓ Running tests..."
./gradlew test

# Build JAR
echo "✓ Building JAR..."
./gradlew bootJar

# Check Docker
if docker info > /dev/null 2>&1; then
    echo "✓ Docker is available"
else
    echo "⚠ Docker is not available (optional)"
fi

echo ""
echo "✅ All verifications passed!"
echo ""
echo "Next steps:"
echo "  1. Start PostgreSQL: docker-compose up -d postgres"
echo "  2. Run migrations: psql -U gestion_user -d gestion_erp < db/migrations/V001__initial_schema.sql"
echo "  3. Start app: ./gradlew bootRun"
echo "  OR use: ./dev-start.sh"
