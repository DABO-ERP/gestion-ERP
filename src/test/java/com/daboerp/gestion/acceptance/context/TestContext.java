package com.daboerp.gestion.acceptance.context;

import com.daboerp.gestion.api.dto.DocumentTypeResponse;
import com.daboerp.gestion.api.dto.GuestResponse;
import com.daboerp.gestion.api.dto.ReservationResponse;
import com.daboerp.gestion.api.dto.RoomResponse;
import com.daboerp.gestion.api.dto.RoomTypeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test context for sharing state between Cucumber step definitions.
 * This class holds the test data and responses during scenario execution.
 */
@Component
public class TestContext {

    // HTTP Response tracking
    private ResponseEntity<?> lastResponse;
    private String lastErrorMessage;

    // Guest-related test data
    private GuestResponse createdGuest;
    private GuestResponse existingGuest;
    private GuestResponse retrievedGuest;
    private GuestResponse updatedGuest;
    private List<GuestResponse> guestList;

    // Reservation-related test data
    private ReservationResponse createdReservation;
    private ReservationResponse existingReservation;
    private ReservationResponse checkedInReservation;
    private ReservationResponse checkedOutReservation;
    private List<ReservationResponse> reservationList;

    // Room-related test data
    private RoomResponse createdRoom;
    private RoomResponse existingRoom;
    private List<RoomResponse> roomList;
    private List<RoomResponse> availableRooms;

    // Room Type-related test data
    private RoomTypeResponse createdRoomType;
    private RoomTypeResponse existingRoomType;
    private List<RoomTypeResponse> roomTypeList;

    // Document Type-related test data
    private DocumentTypeResponse createdDocumentType;
    private DocumentTypeResponse existingDocumentType;
    private DocumentTypeResponse retrievedDocumentType;
    private List<DocumentTypeResponse> documentTypeList;

    // Generic storage for test data
    private Map<String, Object> testData = new HashMap<>();
    
    // Entity lookup maps for relating external identifiers to internal IDs
    private Map<String, String> guestEmailToIdMap = new HashMap<>();
    private Map<Integer, String> roomNumberToIdMap = new HashMap<>();

    // HTTP Response methods
    public void setLastResponse(ResponseEntity<?> response) {
        this.lastResponse = response;
    }

    public ResponseEntity<?> getLastResponse() {
        return lastResponse;
    }

    public void setLastErrorMessage(String errorMessage) {
        this.lastErrorMessage = errorMessage;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    // Guest-related methods
    public void setCreatedGuest(GuestResponse guest) {
        this.createdGuest = guest;
    }

    public GuestResponse getCreatedGuest() {
        return createdGuest;
    }

    public void setExistingGuest(GuestResponse guest) {
        this.existingGuest = guest;
    }

    public GuestResponse getExistingGuest() {
        return existingGuest;
    }

    public void setRetrievedGuest(GuestResponse guest) {
        this.retrievedGuest = guest;
    }

    public GuestResponse getRetrievedGuest() {
        return retrievedGuest;
    }

    public void setUpdatedGuest(GuestResponse guest) {
        this.updatedGuest = guest;
    }

    public GuestResponse getUpdatedGuest() {
        return updatedGuest;
    }

    public void setGuestList(List<GuestResponse> guests) {
        this.guestList = guests;
    }

    public List<GuestResponse> getGuestList() {
        return guestList;
    }

    // Reservation-related methods
    public void setCreatedReservation(ReservationResponse reservation) {
        this.createdReservation = reservation;
    }

    public ReservationResponse getCreatedReservation() {
        return createdReservation;
    }

    public void setExistingReservation(ReservationResponse reservation) {
        this.existingReservation = reservation;
    }

    public ReservationResponse getExistingReservation() {
        return existingReservation;
    }

    public void setCheckedInReservation(ReservationResponse reservation) {
        this.checkedInReservation = reservation;
    }

    public ReservationResponse getCheckedInReservation() {
        return checkedInReservation;
    }

    public void setCheckedOutReservation(ReservationResponse reservation) {
        this.checkedOutReservation = reservation;
    }

    public ReservationResponse getCheckedOutReservation() {
        return checkedOutReservation;
    }

    public void setReservationList(List<ReservationResponse> reservations) {
        this.reservationList = reservations;
    }

    public List<ReservationResponse> getReservationList() {
        return reservationList;
    }

    // Room-related methods
    public void setCreatedRoom(RoomResponse room) {
        this.createdRoom = room;
    }

    public RoomResponse getCreatedRoom() {
        return createdRoom;
    }

    public void setExistingRoom(RoomResponse room) {
        this.existingRoom = room;
    }

    public RoomResponse getExistingRoom() {
        return existingRoom;
    }

    public void setRoomList(List<RoomResponse> rooms) {
        this.roomList = rooms;
    }

    public List<RoomResponse> getRoomList() {
        return roomList;
    }

    public void setAvailableRooms(List<RoomResponse> rooms) {
        this.availableRooms = rooms;
    }

    public List<RoomResponse> getAvailableRooms() {
        return availableRooms;
    }

    // Room Type-related methods
    public void setCreatedRoomType(RoomTypeResponse roomType) {
        this.createdRoomType = roomType;
    }

    public RoomTypeResponse getCreatedRoomType() {
        return createdRoomType;
    }

    public void setExistingRoomType(RoomTypeResponse roomType) {
        this.existingRoomType = roomType;
    }

    public RoomTypeResponse getExistingRoomType() {
        return existingRoomType;
    }

    public void setRoomTypeList(List<RoomTypeResponse> roomTypes) {
        this.roomTypeList = roomTypes;
    }

    public List<RoomTypeResponse> getRoomTypeList() {
        return roomTypeList;
    }

    // Document Type-related methods
    public void setCreatedDocumentType(DocumentTypeResponse documentType) {
        this.createdDocumentType = documentType;
    }

    public DocumentTypeResponse getCreatedDocumentType() {
        return createdDocumentType;
    }

    public void setExistingDocumentType(DocumentTypeResponse documentType) {
        this.existingDocumentType = documentType;
    }

    public DocumentTypeResponse getExistingDocumentType() {
        return existingDocumentType;
    }

    public void setRetrievedDocumentType(DocumentTypeResponse documentType) {
        this.retrievedDocumentType = documentType;
    }

    public DocumentTypeResponse getRetrievedDocumentType() {
        return retrievedDocumentType;
    }

    public void setDocumentTypeList(List<DocumentTypeResponse> documentTypes) {
        this.documentTypeList = documentTypes;
    }

    public List<DocumentTypeResponse> getDocumentTypeList() {
        return documentTypeList;
    }

    // Generic data storage methods
    public void setTestData(String key, Object value) {
        this.testData.put(key, value);
    }

    public Object getTestData(String key) {
        return testData.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTestData(String key, Class<T> type) {
        return (T) testData.get(key);
    }

    public boolean hasTestData(String key) {
        return testData.containsKey(key);
    }

    public void clearTestData() {
        this.testData.clear();
    }

    /**
     * Clear all test context data. 
     * Should be called at the beginning of each scenario.
     */
    public void clearAll() {
        this.lastResponse = null;
        this.lastErrorMessage = null;
        
        this.createdGuest = null;
        this.existingGuest = null;
        this.retrievedGuest = null;
        this.updatedGuest = null;
        this.guestList = null;
        
        this.createdReservation = null;
        this.existingReservation = null;
        this.checkedInReservation = null;
        this.checkedOutReservation = null;
        this.reservationList = null;
        
        this.createdRoom = null;
        this.existingRoom = null;
        this.roomList = null;
        this.availableRooms = null;
        
        this.createdRoomType = null;
        this.existingRoomType = null;
        this.roomTypeList = null;
        
        this.testData.clear();
        this.guestEmailToIdMap.clear();
        this.roomNumberToIdMap.clear();
    }
    
    // Entity lookup methods
    public void registerGuestEmailToId(String email, String id) {
        this.guestEmailToIdMap.put(email, id);
    }
    
    public String getGuestIdByEmail(String email) {
        return this.guestEmailToIdMap.get(email);
    }
    
    public void registerRoomNumberToId(Integer roomNumber, String id) {
        this.roomNumberToIdMap.put(roomNumber, id);
    }
    
    public String getRoomIdByNumber(Integer roomNumber) {
        return this.roomNumberToIdMap.get(roomNumber);
    }
}