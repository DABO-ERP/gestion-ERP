Feature: Document Type Management
  As a hotel/hostel staff member
  I want to manage document types for guest identification
  So that I can properly validate and store guest identification documents

  Background:
    Given the system is running
    And the database is clean

  Scenario: List all document types
    Given the following document types exist in the system:
      | code     | name              | description                    | validationRegex  | active |
      | PASSPORT | Passport          | International travel document | ^[A-Z]{1,3}[0-9]{6,9}$ | true   |
      | DNI      | National ID       | National identification card   | ^[0-9]{7,10}$    | true   |
      | LICENSE  | Driver License    | Driving license document       | ^[A-Z0-9]{8,12}$ | true   |
      | CEDULA   | Cédula            | Latin American ID card         | ^[0-9]{8,11}$    | true   |
      | OLDID    | Deprecated ID     | Old identification type        | ^[0-9]{6}$       | false  |
    When I request all document types
    Then I should receive 5 document types
    And the document types should include:
      | code     | name              | active |
      | PASSPORT | Passport          | true   |
      | DNI      | National ID       | true   |
      | LICENSE  | Driver License    | true   |
      | CEDULA   | Cédula            | true   |
      | OLDID    | Deprecated ID     | false  |
    And each document type should have the required fields:
      | field            |
      | code             |
      | name             |
      | description      |
      | validationRegex  |
      | active           |

  Scenario: List only active document types
    Given the following document types exist in the system:
      | code     | name              | active |
      | PASSPORT | Passport          | true   |
      | DNI      | National ID       | true   |
      | OLDID    | Deprecated ID     | false  |
    When I request only active document types
    Then I should receive 2 document types
    And all returned document types should be active
    And the active document types should be:
      | code     | name              |
      | PASSPORT | Passport          |
      | DNI      | National ID       |

  Scenario: Create a new document type successfully
    When I create a document type with the following details:
      | code            | VISA             |
      | name            | Visa Document    |
      | description     | Temporary stay visa |
      | validationRegex | ^V[0-9]{6,8}$    |
      | active          | true             |
    Then the document type should be created successfully
    And the document type should have the code "VISA"
    And the document type should be active

  Scenario: Create document type with duplicate code
    Given a document type with code "PASSPORT" already exists
    When I create a new document type with code "PASSPORT"
    Then the document type creation should fail
    And I should receive an error about duplicate document type code

  Scenario: Retrieve document type by code
    Given a document type exists with the following details:
      | code            | DNI              |
      | name            | National ID      |
      | description     | National identification card |
      | validationRegex | ^[0-9]{7,10}$    |
      | active          | true             |
    When I request the document type by code "DNI"
    Then I should receive the document type details
    And the details should match the created document type

  Scenario: Search document types by name
    Given the following document types exist in the system:
      | code     | name              | active |
      | PASSPORT | Passport          | true   |
      | LICENSE  | Driver License    | true   |
      | OLDLIC   | Old License       | false  |
    When I search for document types containing "License"
    Then I should receive 2 document types
    And the returned document types should be:
      | code     | name              |
      | LICENSE  | Driver License    |
      | OLDLIC   | Old License       |

  Scenario: List document types with pagination
    Given there are 15 document types in the system
    When I request document types with page size 5 and page number 2
    Then I should receive 5 document types
    And the response should indicate page 2 of 3 total pages
    And the response should include pagination metadata