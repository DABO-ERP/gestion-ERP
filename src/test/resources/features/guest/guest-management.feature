Feature: Guest Management
  As a hotel/hostel staff member
  I want to manage guest information
  So that I can track customer details and provide personalized service

  Background:
    Given the system is running
    And the database is clean

  Scenario: Register a new guest successfully
    When I create a new guest with the following details:
      | firstName    | John           |
      | lastName     | Doe            |
      | email        | john@email.com |
      | phone        | +1234567890    |
      | dateOfBirth  | 1985-06-15     |
      | nationality  | UNITED_STATES  |
      | documentNumber | A12345678    |
      | documentType | PASSPORT       |
    Then the guest should be created successfully
    And the guest should have a unique ID
    And the guest creation event should be published

  Scenario: Register guest with invalid email
    When I create a new guest with invalid email "invalid-email"
    Then the guest creation should fail
    And I should receive a validation error about invalid email format

  Scenario: Register duplicate guest
    Given a guest exists with email "existing@email.com"
    When I create a new guest with email "existing@email.com"
    Then the guest creation should fail
    And I should receive an error about duplicate email

  Scenario: Retrieve guest information
    Given a guest exists with the following details:
      | firstName    | Jane           |
      | lastName     | Smith          |
      | email        | jane@email.com |
      | phone        | +1987654321    |
      | dateOfBirth  | 1990-03-20     |
      | nationality  | CANADA         |
      | documentNumber | B98765432    |
      | documentType | PASSPORT       |
    When I request the guest information by ID
    Then I should receive the guest details
    And the details should match the created guest

  Scenario: List all guests
    Given the following guests exist:
      | firstName | lastName | email           |
      | Alice     | Johnson  | alice@email.com |
      | Bob       | Wilson   | bob@email.com   |
      | Carol     | Brown    | carol@email.com |
    When I request the list of all guests
    Then I should receive 3 guests
    And the list should contain all created guests

  Scenario: Update guest contact information
    Given a guest exists with email "update@email.com"
    When I update the guest's contact information:
      | email | newemail@email.com |
      | phone | +1555000123        |
    Then the guest's contact information should be updated
    And the new information should be persisted