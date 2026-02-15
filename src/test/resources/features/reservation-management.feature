Feature: Reservation Management
  As a hotel/hostel staff member
  I want to manage reservations
  So that I can handle guest bookings and room occupancy

  Background:
    Given the system is running
    And the database is clean
    And the following room types exist:
      | name     | description      | basePrice | maxOccupancy |
      | Single   | Single bed room  | 50.00     | 1            |
      | Double   | Double bed room  | 80.00     | 2            |
      | Dormitory| Shared dormitory | 25.00     | 6            |
    And the following rooms exist:
      | number | roomType  | status    |
      | 101    | Single    | AVAILABLE |
      | 102    | Double    | AVAILABLE |
      | 201    | Dormitory | AVAILABLE |

  Scenario: Create a new reservation successfully
    Given a guest exists with email "guest@email.com"
    When I create a reservation with the following details:
      | guestEmail  | guest@email.com |
      | checkIn     | 2026-03-01      |
      | checkOut    | 2026-03-05      |
      | roomNumber  | 101             |
      | totalAmount | 200.00          |
      | source      | WEBSITE         |
    Then the reservation should be created successfully
    And the reservation should have a unique code
    And the reservation status should be CONFIRMED
    And the reservation creation event should be published

  Scenario: Create reservation with invalid dates
    Given a guest exists with email "guest@email.com"
    When I create a reservation with check-out date before check-in date
    Then the reservation creation should fail
    And I should receive a validation error about invalid dates

  Scenario: Create reservation for non-existent guest
    When I create a reservation for non-existent guest "nonexistent@email.com"
    Then the reservation creation should fail
    And I should receive an error about guest not found

  Scenario: Create reservation for unavailable room
    Given a guest exists with email "guest@email.com"
    And room "101" is occupied from "2026-03-01" to "2026-03-05"
    When I create a reservation for room "101" from "2026-03-02" to "2026-03-04"
    Then the reservation creation should fail
    And I should receive an error about room availability

  Scenario: Check-in a confirmed reservation
    Given a guest exists with email "checkin@email.com"
    And a confirmed reservation exists for the guest
    When I check-in the reservation
    Then the reservation status should be CHECKED_IN
    And the check-in date should be recorded
    And the room status should be OCCUPIED

  Scenario: Check-out a checked-in reservation
    Given a guest exists with email "checkout@email.com"
    And a checked-in reservation exists for the guest
    When I check-out the reservation
    Then the reservation status should be CHECKED_OUT
    And the check-out date should be recorded
    And the room status should be AVAILABLE

  Scenario: List reservations for a date range
    Given a guest exists with email "list@email.com"
    And the following reservations exist:
      | guestEmail   | checkIn    | checkOut   | status    |
      | list@email.com| 2026-03-01 | 2026-03-03 | CONFIRMED |
      | list@email.com| 2026-03-10 | 2026-03-12 | CONFIRMED |
      | list@email.com| 2026-04-01 | 2026-04-03 | CONFIRMED |
    When I request reservations for March 2026
    Then I should receive 2 reservations
    And all reservations should be within the requested date range

  Scenario: Calculate pricing based on stay duration
    Given a guest exists with email "pricing@email.com"
    When I create a reservation for 7 nights in a Single room
    Then the system should apply long stay discount
    And the total amount should reflect the discounted price