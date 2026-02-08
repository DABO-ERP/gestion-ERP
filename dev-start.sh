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
docker-compose up -d postgres

# Wait for PostgreSQL to be ready
echo "⏳ Waiting for PostgreSQL to be ready..."
sleep 5

# Run migrations
echo "🔄 Running database migrations..."
docker-compose exec -T postgres psql -U gestion_user -d gestion_erp -f /docker-entrypoint-initdb.d/V001__initial_schema.sql || true
docker-compose exec -T postgres psql -U gestion_user -d gestion_erp -f /docker-entrypoint-initdb.d/V002__seed_data.sql || true

# Start application
echo "🏃 Starting Gestion ERP application..."
./gradlew bootRun --args='--spring.profiles.active=dev'
