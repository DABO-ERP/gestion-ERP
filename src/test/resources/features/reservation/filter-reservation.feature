Feature: Filter Reservations
  As a hotel manager  
  I want to filter reservations by various criteria with pagination
  So that I can efficiently manage and analyze booking data

  Background:
    Given the database is clean

  Scenario: Filter reservations by status - CONFIRMED
    Given the following guests exist:
      | firstName | lastName | email                | phone         | dateOfBirth | nationality     | documentNumber | documentType |
      | John      | Doe      | john@example.com     | +1234567890   | 1985-05-15  | UNITED_STATES  | A12345678      | PASSPORT     |
      | Jane      | Smith    | jane@example.com     | +1234567891   | 1990-08-20  | CANADA         | B98765432      | PASSPORT     |
      | Carlos    | Garcia   | carlos@example.com   | +1234567892   | 1988-03-10  | SPAIN          | C11223344      | PASSPORT     |
    And the following room types exist:
      | name         | description      | basePrice | maxOccupancy |
      | Single Room  | Single bed room  | 100.00    | 1            |
      | Double Room  | Double bed room  | 150.00    | 2            |
    And the following rooms exist:
      | number | roomType     | status    |
      | 101    | Single Room  | AVAILABLE |
      | 102    | Double Room  | AVAILABLE |
      | 201    | Double Room  | AVAILABLE |
    And the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CHECKED_IN  | HOSTELWORLD  | 600.00       |
    When I filter reservations by status "CONFIRMED"
    Then the response should be successful
    And I should receive 2 reservations
    And all returned reservations should have status "CONFIRMED"

  Scenario: Filter reservations by status - CHECKED_IN
    Given the following guests exist:
      | firstName | lastName | email                | phone         | dateOfBirth | nationality     | documentNumber | documentType |
      | John      | Doe      | john@example.com     | +1234567890   | 1985-05-15  | UNITED_STATES  | A12345678      | PASSPORT     |
      | Jane      | Smith    | jane@example.com     | +1234567891   | 1990-08-20  | CANADA         | B98765432      | PASSPORT     |
      | Carlos    | Garcia   | carlos@example.com   | +1234567892   | 1988-03-10  | SPAIN          | C11223344      | PASSPORT     |
    And the following room types exist:
      | name           | description         | basePrice | maxOccupancy |
      | Single Room 2  | Single bed room 2   | 100.00    | 1            |
      | Double Room 2  | Double bed room 2   | 150.00    | 2            |
    And the following rooms exist:
      | number | roomType       | status    |
      | 101    | Single Room 2  | AVAILABLE |
      | 102    | Double Room 2  | AVAILABLE |
      | 201    | Double Room 2  | AVAILABLE |
    And the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CHECKED_IN  | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CHECKED_IN  | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CONFIRMED   | HOSTELWORLD  | 600.00       |
    When I filter reservations by status "CHECKED_IN"
    Then the response should be successful
    And I should receive 2 reservations
    And all returned reservations should have status "CHECKED_IN"

  @ignore
  Scenario: Filter reservations by source - BOOKING
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CHECKED_IN  | BOOKING      | 600.00       |
      | emma@example.com     | 301        | 2026-03-20 | 2026-03-25 | PENDING     | HOSTELWORLD  | 1500.00      |
    When I filter reservations by source "BOOKING"
    Then the response should be successful
    And I should receive 2 reservations
    And all returned reservations should have source "BOOKING"

  @ignore
  Scenario: Filter reservations by source - DIRECT
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CHECKED_IN  | DIRECT       | 600.00       |
    When I filter reservations by source "DIRECT"
    Then the response should be successful
    And I should receive 2 reservations
    And all returned reservations should have source "DIRECT"

  @ignore
  Scenario: Filter reservations by stay (check-in date)
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CHECKED_IN  | HOSTELWORLD  | 600.00       |
      | emma@example.com     | 301        | 2026-04-01 | 2026-04-05 | CONFIRMED   | AIRBNB       | 1500.00      |
    When I filter reservations by check-in date from "2026-03-01" to "2026-03-31"
    Then the response should be successful
    And I should receive 3 reservations
    And all returned reservations should have check-in dates between "2026-03-01" and "2026-03-31"

  @ignore
  Scenario: Filter reservations by stay (overlapping dates)
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CHECKED_IN  | HOSTELWORLD  | 600.00       |
      | emma@example.com     | 301        | 2026-03-20 | 2026-03-25 | CONFIRMED   | AIRBNB       | 1500.00      |
    When I filter reservations overlapping with dates "2026-03-09" to "2026-03-13"
    Then the response should be successful
    And I should receive 2 reservations

  @ignore
  Scenario: Filter reservations with combined filters - status and source
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
      | carlos@example.com   | 201        | 2026-03-08 | 2026-03-12 | CONFIRMED   | BOOKING      | 600.00       |
      | emma@example.com     | 301        | 2026-03-20 | 2026-03-25 | CHECKED_IN  | BOOKING      | 1500.00      |
    When I filter reservations by status "CONFIRMED" and source "BOOKING"
    Then the response should be successful
    And I should receive 2 reservations
    And all returned reservations should have status "CONFIRMED"
    And all returned reservations should have source "BOOKING"

  @ignore
  Scenario: Filter reservations with pagination - first page
    Given there are 15 reservations in the system
    When I filter reservations with page size 5 and page number 1
    Then the response should be successful
    And I should receive 5 reservations
    And the reservation response should indicate page 1 of 3 total pages
    And the reservation response should include pagination metadata

  @ignore
  Scenario: Filter reservations with pagination - second page
    Given there are 15 reservations in the system
    When I filter reservations with page size 5 and page number 2
    Then the response should be successful
    And I should receive 5 reservations
    And the reservation response should indicate page 2 of 3 total pages
    And the reservation response should include pagination metadata

  @ignore
  Scenario: Filter reservations with pagination - last page
    Given there are 15 reservations in the system
    When I filter reservations with page size 5 and page number 3
    Then the response should be successful
    And I should receive 5 reservations
    And the reservation response should indicate page 3 of 3 total pages
    And the reservation response should include pagination metadata

  @ignore
  Scenario: Filter reservations by status with pagination
    Given the following 12 reservations exist with status:
      | count | status     |
      | 8     | CONFIRMED  |
      | 4     | CHECKED_IN |
    When I filter reservations by status "CONFIRMED" with page size 3 and page number 1
    Then the response should be successful
    And I should receive 3 reservations
    And the reservation response should indicate page 1 of 3 total pages
    And all returned reservations should have status "CONFIRMED"

  @ignore
  Scenario: Filter reservations with no results
    Given the following reservations exist:
      | guestEmail           | roomNumber | checkIn    | checkOut   | status      | source       | quotedAmount |
      | john@example.com     | 101        | 2026-03-01 | 2026-03-05 | CONFIRMED   | DIRECT       | 400.00       |
      | jane@example.com     | 102        | 2026-03-10 | 2026-03-15 | CONFIRMED   | BOOKING      | 750.00       |
    When I filter reservations by status "CANCELLED"
    Then the response should be successful
    And I should receive 0 reservations

  @ignore  
  Scenario: Filter reservations with invalid status
    When I filter reservations by status "INVALID_STATUS"
    Then the response should fail with status code 400

  @ignore
  Scenario: Filter reservations with invalid source
    When I filter reservations by source "INVALID_SOURCE"
    Then the response should fail with status code 400

  @ignore
  Scenario: Filter reservations with invalid pagination parameters
    When I filter reservations with page size -1 and page number 1
    Then the response should fail with status code 400
