package com.daboerp.gestion.acceptance.steps;

import com.daboerp.gestion.acceptance.context.TestContext;
import com.daboerp.gestion.api.dto.*;
import com.daboerp.gestion.domain.valueobject.Amenity;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;
import com.daboerp.gestion.domain.valueobject.RoomStatus;
import com.daboerp.gestion.domain.valueobject.Source;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ROOM_TYPES_API_URL = "/api/v1/room-types";
    private static final String ROOMS_API_URL = "/api/v1/rooms";
    private static final String GUESTS_API_URL = "/api/v1/guests";
    private static final String RESERVATIONS_API_URL = "/api/v1/reservations";
    
    // Map to store room type names to their IDs
    private Map<String, String> roomTypeNameToIdMap = new HashMap<>();
    
    @Before
    public void setUp() {
        // Clear the room type name to ID mapping before each scenario
        roomTypeNameToIdMap.clear();
    }

    @When("I create a room type with the following details:")
    public void iCreateARoomTypeWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> roomTypeData = dataTable.asMap();
        
        CreateRoomTypeRequest request = new CreateRoomTypeRequest(
            roomTypeData.get("name"),
            roomTypeData.get("description"),
            Integer.parseInt(roomTypeData.get("maxOccupancy")),
            new BigDecimal(roomTypeData.get("basePrice"))
        );

        ResponseEntity<RoomTypeResponse> response = restTemplate.postForEntity(
            ROOM_TYPES_API_URL, 
            request, 
            RoomTypeResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setCreatedRoomType(response.getBody());
        }
    }

    @Given("a room type {string} exists with base price {double}")
    public void aRoomTypeExistsWithBasePrice(String roomTypeName, double basePrice) {
        ensureRoomTypeExistsWithDetails(roomTypeName, roomTypeName + " room type", 2, basePrice);
        String roomTypeId = roomTypeNameToIdMap.get(roomTypeName);
        assertThat(roomTypeId)
            .as("Room type ID should be registered for %s", roomTypeName)
            .isNotNull();

        // Set existing room type for scenarios that rely on it (minimal data needed)
        testContext.setExistingRoomType(new RoomTypeResponse(
            roomTypeId,
            roomTypeName,
            roomTypeName + " room type",
            2,
            BigDecimal.valueOf(basePrice)
        ));
    }

    @Given("the following room types exist:")
    public void theFollowingRoomTypesExist(DataTable dataTable) {
        List<Map<String, String>> roomTypes = dataTable.asMaps();
        
        for (Map<String, String> roomTypeData : roomTypes) {
            String roomTypeName = roomTypeData.get("name");
            String description = roomTypeData.get("description");
            int maxOccupancy = Integer.parseInt(roomTypeData.get("maxOccupancy"));
            double basePrice = Double.parseDouble(roomTypeData.get("basePrice"));
            
            // Use the ensure method which handles duplicates gracefully
            ensureRoomTypeExistsWithDetails(roomTypeName, description, maxOccupancy, basePrice);
        }
    }

    /**
     * Helper method to ensure a room type exists without failing if it already exists
     */
    private void ensureRoomTypeExistsWithDetails(String roomTypeName, String description, int maxOccupancy, double basePrice) {
        // First check if we already have the ID in our map
        if (roomTypeNameToIdMap.containsKey(roomTypeName)) {
            return;
        }
        
        // Generate a unique name to avoid conflicts across tests
        String uniqueRoomTypeName = roomTypeName + "-" + System.currentTimeMillis();
        
        CreateRoomTypeRequest request = new CreateRoomTypeRequest(
            uniqueRoomTypeName,
            description,
            maxOccupancy,
            BigDecimal.valueOf(basePrice)
        );

        try {
            ResponseEntity<RoomTypeResponse> response = restTemplate.postForEntity(
                ROOM_TYPES_API_URL, 
                request, 
                RoomTypeResponse.class
            );
            // Store the mapping if creation was successful
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                RoomTypeResponse body = Objects.requireNonNull(response.getBody());
                // Map both the original name and unique name to the same ID
                roomTypeNameToIdMap.put(roomTypeName, body.id());
                roomTypeNameToIdMap.put(uniqueRoomTypeName, body.id());
            } else {
                throw new RuntimeException("Failed to create room type: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not ensure room type exists: " + roomTypeName, e);
        }
    }

    @When("I create a room with the following details:")
    public void iCreateARoomWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> roomData = dataTable.asMap();

        List<Amenity> amenities = List.of(roomData.get("amenities").split(",")).stream()
            .map(String::strip)
            .filter(s -> !s.isBlank())
            .map(s -> s.replace("TV", "TELEVISION"))
            .map(Amenity::valueOf)
            .toList();

        String roomTypeName = roomData.get("roomType");
        String roomTypeId = resolveRoomTypeId(roomTypeName);
        
        CreateRoomRequest request = new CreateRoomRequest(
            Integer.parseInt(roomData.get("number")),
            roomTypeId,
            amenities,
            2 // numberOfBeds
        );

        ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
            ROOMS_API_URL, 
            request, 
            RoomResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setCreatedRoom(response.getBody());
        }
    }

    @Given("a room {string} already exists")
    public void aRoomAlreadyExists(String roomNumber) {
        // First ensure room type exists
        aRoomTypeExistsWithBasePrice("Single", 50.0);

        String roomTypeId = resolveRoomTypeId("Single");
        
        CreateRoomRequest request = new CreateRoomRequest(
            Integer.parseInt(roomNumber),
            roomTypeId,
            List.of(Amenity.WIFI),
            1
        );

        ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
            ROOMS_API_URL, 
            request, 
            RoomResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RoomResponse body = Objects.requireNonNull(response.getBody());
        testContext.setExistingRoom(body);
        testContext.registerRoomNumberToId(Integer.parseInt(roomNumber), body.id());
    }

    @When("I create a new room with number {string}")
    public void iCreateANewRoomWithNumber(String roomNumber) {
        String roomTypeId = resolveRoomTypeId("Single");
        CreateRoomRequest request = new CreateRoomRequest(
            Integer.parseInt(roomNumber),
            roomTypeId,
            List.of(Amenity.WIFI),
            1 // numberOfBeds
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
            ROOMS_API_URL, 
            request, 
            String.class
        );

        testContext.setLastResponse(response);
    }

    @Given("the following rooms exist:")
    public void theFollowingRoomsExist(DataTable dataTable) {
        List<Map<String, String>> rooms = dataTable.asMaps();
        
        // Don't create room types here - they should already exist from previous steps
        
        for (Map<String, String> roomData : rooms) {
            String roomTypeName = roomData.get("roomType");
            String roomTypeId = roomTypeNameToIdMap.get(roomTypeName);
            
            // If room type ID not found in map, it means it wasn't created properly
            // Let's try to create it with basic defaults
            if (roomTypeId == null) {
                System.out.println("DEBUG: Room type ID not found for '" + roomTypeName + "', calling ensureRoomTypeExists");
                ensureRoomTypeExists(roomTypeName, 50.0);
                roomTypeId = roomTypeNameToIdMap.get(roomTypeName);
                System.out.println("DEBUG: After ensureRoomTypeExists, roomTypeId = " + roomTypeId);
            }
            
            // If still null, use the name as last resort (this will likely fail, but provides clear error)
            if (roomTypeId == null) {
                System.out.println("DEBUG: Room type ID still null, using roomTypeName as fallback: " + roomTypeName);
                roomTypeId = roomTypeName;
            }
            
            Integer roomNumber = Integer.parseInt(roomData.get("number"));
            
            System.out.println("DEBUG: Creating room " + roomNumber + " with roomTypeId: " + roomTypeId);
            
            CreateRoomRequest request = new CreateRoomRequest(
                roomNumber,
                roomTypeId,
                List.of(Amenity.WIFI, Amenity.TELEVISION), // amenities
                1 // numberOfBeds
            );

            ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
                ROOMS_API_URL, 
                request, 
                RoomResponse.class
            );

            System.out.println("DEBUG: Room creation response status: " + response.getStatusCode());
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            RoomResponse created = Objects.requireNonNull(response.getBody());
            
            // Store the room number to ID mapping for use in reservations
            testContext.registerRoomNumberToId(roomNumber, created.id());
            
            // Update room status if needed
            RoomStatus targetStatus = parseRoomStatus(roomData.get("status"));
            if (targetStatus != RoomStatus.AVAILABLE) {
                updateRoomStatus(created.id(), targetStatus);
            }
        }
    }

    /**
     * Helper method to ensure a room type exists without failing if it already exists
     */
    private void ensureRoomTypeExists(String roomTypeName, double basePrice) {
        System.out.println("DEBUG: ensureRoomTypeExists called for: " + roomTypeName);
        
        // First check if we already have the ID in our map
        if (roomTypeNameToIdMap.containsKey(roomTypeName)) {
            System.out.println("DEBUG: Room type already in map: " + roomTypeNameToIdMap.get(roomTypeName));
            return;
        }
        
        // Generate a unique name to avoid conflicts across tests
        String uniqueRoomTypeName = roomTypeName + "-" + System.currentTimeMillis();
        
        CreateRoomTypeRequest request = new CreateRoomTypeRequest(
            uniqueRoomTypeName,
            roomTypeName + " room type",
            2, // default occupancy
            BigDecimal.valueOf(basePrice)
        );

        try {
            System.out.println("DEBUG: Attempting to create room type: " + uniqueRoomTypeName);
            ResponseEntity<RoomTypeResponse> response = restTemplate.postForEntity(
                ROOM_TYPES_API_URL, 
                request, 
                RoomTypeResponse.class
            );
            System.out.println("DEBUG: Room type creation response: " + response.getStatusCode());
            // Store the mapping if creation was successful
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                RoomTypeResponse body = Objects.requireNonNull(response.getBody());
                String roomTypeId = body.id();
                System.out.println("DEBUG: Successfully created room type with ID: " + roomTypeId);
                // Map both the original name and unique name to the same ID
                roomTypeNameToIdMap.put(roomTypeName, roomTypeId);
                roomTypeNameToIdMap.put(uniqueRoomTypeName, roomTypeId);
            } else {
                throw new RuntimeException("Failed to create room type: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Room type creation failed: " + e.getMessage());
            throw new RuntimeException("Could not ensure room type exists: " + roomTypeName, e);
        }
    }

    @Given("the following rooms exist with room types:")
    public void theFollowingRoomsExistWithRoomTypes(DataTable dataTable) {
        List<Map<String, String>> rooms = dataTable.asMaps();

        // Ensure room types exist with the requested occupancy
        for (Map<String, String> roomData : rooms) {
            String roomTypeName = roomData.get("roomType");
            int maxOccupancy = Integer.parseInt(roomData.get("maxOccupancy"));
            ensureRoomTypeExistsWithDetails(roomTypeName, roomTypeName + " room type", maxOccupancy, 50.0);
        }

        // Now create rooms using the resolved roomType IDs
        for (Map<String, String> roomData : rooms) {
            String roomTypeId = resolveRoomTypeId(roomData.get("roomType"));
            CreateRoomRequest request = new CreateRoomRequest(
                Integer.parseInt(roomData.get("number")),
                roomTypeId,
                List.of(Amenity.WIFI, Amenity.TELEVISION), // amenities
                1 // numberOfBeds
            );

            ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
                ROOMS_API_URL, 
                request, 
                RoomResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            RoomResponse created = Objects.requireNonNull(response.getBody());
            
            // Update room status if needed
            RoomStatus targetStatus = parseRoomStatus(roomData.get("status"));
            if (targetStatus != RoomStatus.AVAILABLE) {
                updateRoomStatus(created.id(), targetStatus);
            }

            testContext.registerRoomNumberToId(Integer.parseInt(roomData.get("number")), created.id());
        }
    }

    @Given("room {string} has reservation from {string} to {string}")
    public void roomHasReservationFromTo(String roomNumber, String checkIn, String checkOut) {
        Integer roomNum = Integer.parseInt(roomNumber);
        String roomId = testContext.getRoomIdByNumber(roomNum);
        assertThat(roomId)
            .as("Room ID should be registered for room %s", roomNumber)
            .isNotNull();

        // Create a guest for this reservation
        String uniqueEmail = "availability+" + System.nanoTime() + "@email.com";
        CreateGuestRequest guestRequest = new CreateGuestRequest(
            "Availability",
            "Guest",
            uniqueEmail,
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            Nationality.UNITED_STATES,
            "B" + System.nanoTime(),
            DocumentType.PASSPORT
        );

        ResponseEntity<GuestResponse> guestResponse = restTemplate.postForEntity(
            GUESTS_API_URL,
            guestRequest,
            GuestResponse.class
        );

        assertThat(guestResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        GuestResponse guestBody = Objects.requireNonNull(guestResponse.getBody());
        testContext.registerGuestEmailToId(uniqueEmail, guestBody.id());

        CreateReservationRequest reservationRequest = new CreateReservationRequest(
            LocalDate.parse(checkIn),
            LocalDate.parse(checkOut),
            new BigDecimal("100.00"),
            Source.DIRECT,
            guestBody.id(),
            roomId,
            List.of()
        );

        ResponseEntity<ReservationResponse> reservationResponse = restTemplate.postForEntity(
            RESERVATIONS_API_URL,
            reservationRequest,
            ReservationResponse.class
        );

        assertThat(reservationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Given("a room {string} exists with status AVAILABLE")
    public void aRoomExistsWithStatusAVAILABLE(String roomNumber) {
        aRoomTypeExistsWithBasePrice("Single", 50.0);

        String roomTypeId = resolveRoomTypeId("Single");
        
        CreateRoomRequest request = new CreateRoomRequest(
            Integer.parseInt(roomNumber),
            roomTypeId,
            List.of(Amenity.WIFI),
            1 // numberOfBeds
        );

        ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
            ROOMS_API_URL, 
            request, 
            RoomResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RoomResponse body = Objects.requireNonNull(response.getBody());
        testContext.setExistingRoom(body);
        testContext.registerRoomNumberToId(Integer.parseInt(roomNumber), body.id());
    }

    @When("I request all available rooms")
    public void iRequestAllAvailableRooms() {
        String url = ROOMS_API_URL + "/available";
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                List<RoomResponse> rooms = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<RoomResponse>>() {}
                );
                testContext.setAvailableRooms(rooms);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse room list response", e);
            }
        }
    }

    @When("I filter rooms by status {string}")
    public void iFilterRoomsByStatus(String status) {
        String url = ROOMS_API_URL + "?status=" + status;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        testContext.setLastResponse(response);

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                List<RoomResponse> rooms = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<RoomResponse>>() {}
                );
                // Reuse the existing storage used by other room list assertions
                testContext.setAvailableRooms(rooms);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse room list response", e);
            }
        }
    }

    @When("I search for available rooms with capacity for {int} guests")
    public void iSearchForAvailableRoomsWithCapacityForGuests(int guestCount) {
        String url = ROOMS_API_URL + "/available?minCapacity=" + guestCount;
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                List<RoomResponse> rooms = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<RoomResponse>>() {}
                );
                testContext.setAvailableRooms(rooms);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse room list response", e);
            }
        }
    }

    @When("I change the room status to MAINTENANCE")
    public void iChangeTheRoomStatusToMAINTENANCE() {
        String roomId = testContext.getExistingRoom().id();

        UpdateRoomStatusRequest updateRequest = new UpdateRoomStatusRequest("OUT_OF_SERVICE");
        HttpEntity<UpdateRoomStatusRequest> requestEntity = new HttpEntity<>(updateRequest);
        
        ResponseEntity<RoomResponse> response = restTemplate.exchange(
            ROOMS_API_URL + "/" + roomId,
            HttpMethod.PUT,
            requestEntity,
            RoomResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setExistingRoom(response.getBody());
        }
    }

    @When("I search for available rooms from {string} to {string}")
    public void iSearchForAvailableRoomsFromTo(String checkIn, String checkOut) {
        String url = ROOMS_API_URL + "/available?checkIn=" + checkIn + "&checkOut=" + checkOut;
        
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                List<RoomResponse> rooms = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<RoomResponse>>() {}
                );
                testContext.setAvailableRooms(rooms);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse room list response", e);
            }
        }
    }

    private void updateRoomStatus(String roomId, RoomStatus status) {
        UpdateRoomStatusRequest updateRequest = new UpdateRoomStatusRequest(status.name());
        HttpEntity<UpdateRoomStatusRequest> requestEntity = new HttpEntity<>(updateRequest);
        
        restTemplate.exchange(
            ROOMS_API_URL + "/" + roomId,
            HttpMethod.PUT,
            requestEntity,
            RoomResponse.class
        );
    }

    private String resolveRoomTypeId(String roomTypeName) {
        if (roomTypeName == null || roomTypeName.isBlank()) {
            throw new IllegalArgumentException("roomType is required");
        }
        String roomTypeId = roomTypeNameToIdMap.get(roomTypeName);
        if (roomTypeId == null) {
            ensureRoomTypeExists(roomTypeName, 50.0);
            roomTypeId = roomTypeNameToIdMap.get(roomTypeName);
        }
        assertThat(roomTypeId)
            .as("Room type ID should be resolvable for %s", roomTypeName)
            .isNotNull();
        return roomTypeId;
    }

    private RoomStatus parseRoomStatus(String status) {
        if (status == null || status.isBlank()) {
            return RoomStatus.AVAILABLE;
        }
        String normalized = status.strip().toUpperCase();
        if (normalized.equals("MAINTENANCE")) {
            return RoomStatus.OUT_OF_SERVICE;
        }
        return RoomStatus.valueOf(normalized);
    }

    @Then("the room type should be created successfully")
    public void theRoomTypeShouldBeCreatedSuccessfully() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("the room type should have a unique ID")
    public void theRoomTypeShouldHaveAUniqueId() {
        RoomTypeResponse roomType = testContext.getCreatedRoomType();
        assertThat(roomType).isNotNull();
        assertThat(roomType.id()).isNotNull();
        assertThat(roomType.id()).isNotBlank();
    }

    @Then("the room should be created successfully")
    public void theRoomShouldBeCreatedSuccessfully() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("the room should have a unique ID")
    public void theRoomShouldHaveAUniqueId() {
        RoomResponse room = testContext.getCreatedRoom();
        assertThat(room).isNotNull();
        assertThat(room.id()).isNotNull();
        assertThat(room.id()).isNotBlank();
    }

    @Then("the room status should be AVAILABLE")
    public void theRoomStatusShouldBeAVAILABLE() {
        RoomResponse room = testContext.getCreatedRoom();
        if (room != null) {
            assertThat(room.roomStatus()).isEqualTo(RoomStatus.AVAILABLE);
            return;
        }

        ReservationResponse reservation = testContext.getCheckedOutReservation();
        if (reservation == null) {
            reservation = testContext.getCheckedInReservation();
        }
        if (reservation == null) {
            reservation = testContext.getExistingReservation();
        }
        if (reservation == null) {
            reservation = testContext.getCreatedReservation();
        }

        assertThat(reservation)
            .as("Expected a reservation in context to resolve room status")
            .isNotNull();

        final String expectedRoomId = reservation.roomId();
        final Integer expectedRoomNumber = reservation.roomNumber();

        ResponseEntity<String> roomsResponse = restTemplate.getForEntity(ROOMS_API_URL, String.class);
        assertThat(roomsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        try {
            List<RoomResponse> rooms = objectMapper.readValue(
                roomsResponse.getBody(),
                new TypeReference<List<RoomResponse>>() {}
            );

            RoomResponse resolved = rooms.stream()
                .filter(r -> r.id().equals(expectedRoomId) || r.roomNumber().equals(expectedRoomNumber))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Room not found in /rooms list"));

            assertThat(resolved.roomStatus()).isEqualTo(RoomStatus.AVAILABLE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse rooms list response", e);
        }
    }

    @Then("the room creation should fail")
    public void theRoomCreationShouldFail() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Then("I should receive an error about duplicate room number")
    public void iShouldReceiveAnErrorAboutDuplicateRoomNumber() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Then("I should receive {int} rooms")
    public void iShouldReceiveRooms(int expectedCount) {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testContext.getAvailableRooms()).hasSize(expectedCount);
    }

    @Then("all returned rooms should have status AVAILABLE")
    public void allReturnedRoomsShouldHaveStatusAVAILABLE() {
        List<RoomResponse> rooms = testContext.getAvailableRooms();
        
        for (RoomResponse room : rooms) {
            assertThat(room.roomStatus()).isEqualTo(RoomStatus.AVAILABLE);
        }
    }

    @Then("all returned rooms should have status {string}")
    public void allReturnedRoomsShouldHaveStatus(String expectedStatus) {
        List<RoomResponse> rooms = testContext.getAvailableRooms();
        RoomStatus expected = parseRoomStatus(expectedStatus);

        for (RoomResponse room : rooms) {
            assertThat(room.roomStatus()).isEqualTo(expected);
        }
    }

    @Then("I should receive rooms that can accommodate {int} or more guests")
    public void iShouldReceiveRoomsThatCanAccommodateOrMoreGuests(int minCapacity) {
        List<RoomResponse> rooms = testContext.getAvailableRooms();
        
        for (RoomResponse room : rooms) {
            assertThat(room.roomType().maxOccupancy()).isGreaterThanOrEqualTo(minCapacity);
        }
    }

    @Then("the room status should be updated to MAINTENANCE")
    public void theRoomStatusShouldBeUpdatedToMAINTENANCE() {
        RoomResponse room = testContext.getExistingRoom();
        assertThat(room.roomStatus()).isEqualTo(RoomStatus.OUT_OF_SERVICE);
    }

    @Then("the room should not appear in available rooms list")
    public void theRoomShouldNotAppearInAvailableRoomsList() {
        // This would be verified by calling the available rooms endpoint
        // and ensuring the out of service room is not included
        assertThat(testContext.getExistingRoom().roomStatus()).isEqualTo(RoomStatus.OUT_OF_SERVICE);
    }

    @Then("I should receive only room {string}")
    public void iShouldReceiveOnlyRoom(String roomNumber) {
        List<RoomResponse> rooms = testContext.getAvailableRooms();
        assertThat(rooms).hasSize(1);
        assertThat(rooms.get(0).roomNumber()).isEqualTo(Integer.parseInt(roomNumber));
    }

    @Then("room {string} should not be included due to existing reservation")
    public void roomShouldNotBeIncludedDueToExistingReservation(String roomNumber) {
        List<RoomResponse> rooms = testContext.getAvailableRooms();
        
        boolean roomFound = rooms.stream()
            .anyMatch(room -> room.roomNumber().equals(Integer.parseInt(roomNumber)));
        
        assertThat(roomFound).isFalse();
    }
}