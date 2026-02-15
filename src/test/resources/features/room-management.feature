Feature: Room Management
  As a hotel/hostel manager
  I want to manage rooms and room types
  So that I can organize accommodation inventory

  Background:
    Given the system is running
    And the database is clean

  Scenario: Create a new room type successfully
    When I create a room type with the following details:
      | name        | Deluxe Suite |
      | description | Luxury room with city view |
      | basePrice   | 150.00       |
      | maxOccupancy| 3            |
    Then the room type should be created successfully
    And the room type should have a unique ID

  Scenario: Create a new room successfully
    Given a room type "Single" exists with base price 50.00
    When I create a room with the following details:
      | number      | 205          |
      | roomType    | Single       |
      | floor       | 2            |
      | description | Quiet room   |
      | amenities   | WIFI, TV     |
    Then the room should be created successfully
    And the room should have a unique ID
    And the room status should be AVAILABLE

  Scenario: Create room with duplicate number
    Given a room "101" already exists
    When I create a new room with number "101"
    Then the room creation should fail
    And I should receive an error about duplicate room number

  Scenario: List all available rooms
    Given the following rooms exist:
      | number | roomType | status      |
      | 101    | Single   | AVAILABLE   |
      | 102    | Double   | OCCUPIED    |
      | 103    | Single   | MAINTENANCE |
      | 104    | Double   | AVAILABLE   |
    When I request all available rooms
    Then I should receive 2 rooms
    And all returned rooms should have status AVAILABLE

  Scenario: Find available rooms by criteria
    Given the following rooms exist with room types:
      | number | roomType  | status    | maxOccupancy |
      | 101    | Single    | AVAILABLE | 1            |
      | 102    | Double    | AVAILABLE | 2            |
      | 103    | Dormitory | AVAILABLE | 6            |
      | 104    | Double    | OCCUPIED  | 2            |
    When I search for available rooms with capacity for 2 guests
    Then I should receive rooms that can accommodate 2 or more guests
    And all returned rooms should have status AVAILABLE

  Scenario: Change room status for maintenance
    Given a room "201" exists with status AVAILABLE
    When I change the room status to MAINTENANCE
    Then the room status should be updated to MAINTENANCE
    And the room should not appear in available rooms list

  Scenario: Search rooms by date availability
    Given the following rooms exist:
      | number | roomType | status    |
      | 301    | Single   | AVAILABLE |
      | 302    | Double   | AVAILABLE |
    And room "301" has reservation from "2026-03-01" to "2026-03-05"
    When I search for available rooms from "2026-03-02" to "2026-03-04"
    Then I should receive only room "302"
    And room "301" should not be included due to existing reservation