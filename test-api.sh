#!/bin/bash

# API Testing Script for Gestion ERP
BASE_URL="http://localhost:8081/api/v1"
echo "🧪 Testing Gestion ERP API"
echo "=========================="
echo ""

# Test 1: Health Check
echo "1️⃣  Testing Health Check..."
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" http://localhost:8081/actuator/health)
http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
body=$(echo "$response" | sed '$d')
if [ "$http_code" = "200" ]; then
    echo "✅ Health check passed: $body"
else
    echo "❌ Health check failed with status $http_code"
fi
echo ""

# Test 2: Create a Guest
echo "2️⃣  Creating a new guest..."
guest_response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/guests" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Perez",
    "email": "juan.perez@example.com",
    "phone": "+57-300-1234567",
    "dateOfBirth": "1985-03-20",
    "nationality": "COLOMBIA",
    "documentNumber": "CC-123456789",
    "documentType": "NATIONAL_ID"
  }')
http_code=$(echo "$guest_response" | grep HTTP_STATUS | cut -d: -f2)
guest_body=$(echo "$guest_response" | sed '$d')
if [ "$http_code" = "201" ]; then
    echo "✅ Guest created successfully"
    guest_id=$(echo "$guest_body" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
    echo "   Guest ID: $guest_id"
else
    echo "❌ Guest creation failed with status $http_code"
    echo "   Response: $guest_body"
fi
echo ""

# Test 3: Get Guest by ID
if [ -n "$guest_id" ]; then
    echo "3️⃣  Getting guest by ID..."
    response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$BASE_URL/guests/$guest_id")
    http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
    if [ "$http_code" = "200" ]; then
        echo "✅ Get guest by ID successful"
    else
        echo "❌ Get guest failed with status $http_code"
    fi
    echo ""
fi

# Test 4: List All Guests
echo "4️⃣  Listing all guests..."
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$BASE_URL/guests")
http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
if [ "$http_code" = "200" ]; then
    echo "✅ List guests successful"
else
    echo "❌ List guests failed with status $http_code"
fi
echo ""

# Test 5: Create a Room Type (first check if one exists)
echo "5️⃣  Creating a room type..."
roomtype_response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/room-types" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Standard Room",
    "description": "Comfortable standard room",
    "maxOccupancy": 2,
    "basePrice": 100.00
  }')
http_code=$(echo "$roomtype_response" | grep HTTP_STATUS | cut -d: -f2)
roomtype_body=$(echo "$roomtype_response" | sed '$d')
if [ "$http_code" = "201" ] || [ "$http_code" = "200" ]; then
    echo "✅ Room type created/exists"
    roomtype_id=$(echo "$roomtype_body" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
    echo "   Room Type ID: $roomtype_id"
else
    echo "❌ Room type creation failed with status $http_code"
    echo "   Response: $roomtype_body"
fi
echo ""

# Test 6: Create a Room
echo "6️⃣  Creating a room..."
# Use existing room type from seed data
room_response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/rooms" \
  -H "Content-Type: application/json" \
  -d '{
    "roomNumber": 501,
    "roomTypeId": "rt-001",
    "amenities": ["BATHROOM", "WIFI", "TELEVISION"],
    "numberOfBeds": 2
  }')
http_code=$(echo "$room_response" | grep HTTP_STATUS | cut -d: -f2)
room_body=$(echo "$room_response" | sed '$d')
if [ "$http_code" = "201" ] || [ "$http_code" = "200" ]; then
    echo "✅ Room created successfully"
    room_id=$(echo "$room_body" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
    echo "   Room ID: $room_id"
else
    echo "⚠️  Room creation status $http_code (might already exist)"
    echo "   Response: $room_body"
fi
echo ""

# Test 7: List All Rooms
echo "7️⃣  Listing all rooms..."
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$BASE_URL/rooms")
http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
if [ "$http_code" = "200" ]; then
    echo "✅ List rooms successful"
else
    echo "❌ List rooms failed with status $http_code"
fi
echo ""

# Test 8: Find Available Rooms
echo "8️⃣  Finding available rooms..."
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$BASE_URL/rooms/available?checkIn=2026-03-01&checkOut=2026-03-05")
http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
if [ "$http_code" = "200" ]; then
    echo "✅ Find available rooms successful"
else
    echo "❌ Find available rooms failed with status $http_code"
fi
echo ""

# Test 9: Create a Reservation
if [ -n "$guest_id" ]; then
    echo "9️⃣  Creating a reservation..."
    reservation_response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X POST "$BASE_URL/reservations" \
      -H "Content-Type: application/json" \
      -d "{
        \"checkIn\": \"2026-03-10\",
        \"checkOut\": \"2026-03-15\",
        \"quotedAmount\": 500.00,
        \"source\": \"DIRECT\",
        \"guestPrincipalId\": \"$guest_id\",
        \"roomId\": \"room-001\",
        \"additionalGuestIds\": []
      }")
    http_code=$(echo "$reservation_response" | grep HTTP_STATUS | cut -d: -f2)
    reservation_body=$(echo "$reservation_response" | sed '$d')
    if [ "$http_code" = "201" ] || [ "$http_code" = "200" ]; then
        echo "✅ Reservation created successfully"
        reservation_id=$(echo "$reservation_body" | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4)
        echo "   Reservation ID: $reservation_id"
    else
        echo "⚠️  Reservation creation status $http_code"
        echo "   Response: $reservation_body"
    fi
    echo ""
fi

# Test 10: List All Reservations
echo "🔟 Listing all reservations..."
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" "$BASE_URL/reservations")
http_code=$(echo "$response" | grep HTTP_STATUS | cut -d: -f2)
if [ "$http_code" = "200" ]; then
    echo "✅ List reservations successful"
else
    echo "❌ List reservations failed with status $http_code"
fi
echo ""

echo "=========================="
echo "✨ API Testing Complete!"
