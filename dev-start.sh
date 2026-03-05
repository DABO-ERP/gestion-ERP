#!/bin/bash

# Development startup script for Gestion ERP

set -e

echo "🚀 Starting Gestion ERP Development Environment..."

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Set development profile
export SPRING_PROFILES_ACTIVE=dev

# Start containers
echo "📦 Starting containers..."
docker compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
timeout=30
elapsed=0
until docker compose exec -T postgres pg_isready -U gestion_user > /dev/null 2>&1 || [ $elapsed -ge $timeout ]; do
    echo "Waiting for PostgreSQL to start... ($elapsed/$timeout seconds)"
    sleep 2
    elapsed=$((elapsed + 2))
done

if [ $elapsed -ge $timeout ]; then
    echo "❌ PostgreSQL failed to start within $timeout seconds"
    exit 1
fi

echo "✅ PostgreSQL is ready!"

# Run migrations (ignore errors if tables already exist)
echo "🔄 Running database migrations..."
docker compose exec -T postgres psql -U gestion_user -d gestion_erp -f /docker-entrypoint-initdb.d/V001__initial_schema.sql 2>&1 | grep -v "already exists" | grep -v "duplicate key" || true
docker compose exec -T postgres psql -U gestion_user -d gestion_erp -f /docker-entrypoint-initdb.d/V002__seed_data.sql 2>&1 | grep -v "already exists" | grep -v "duplicate key" || true

# Build the application
echo "🔨 Building application..."
./gradlew clean compileJava

# Start application
echo "🏃 Starting Gestion ERP application..."
./gradlew bootRun --args='--spring.profiles.active=dev'
