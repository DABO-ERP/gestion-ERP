Feature: Room Filtering
	As a hotel/hostel manager
	I want to filter rooms by status
	So that I can quickly find available, occupied, or out-of-service rooms

	Background:
		Given the system is running
		And the database is clean

	Scenario: Filter rooms by status AVAILABLE
		Given the following rooms exist:
			| number | roomType | status    |
			| 501    | Single   | AVAILABLE |
			| 502    | Double   | OCCUPIED  |
			| 503    | Single   | MAINTENANCE |
			| 504    | Double   | AVAILABLE |
		When I filter rooms by status "AVAILABLE"
		Then I should receive 2 rooms
		And all returned rooms should have status "AVAILABLE"

	Scenario: Filter rooms by status OCCUPIED
		Given the following rooms exist:
			| number | roomType | status    |
			| 511    | Single   | AVAILABLE |
			| 512    | Double   | OCCUPIED  |
			| 513    | Single   | AVAILABLE |
		When I filter rooms by status "OCCUPIED"
		Then I should receive 1 rooms
		And all returned rooms should have status "OCCUPIED"

	Scenario: Filter rooms by status OUT_OF_SERVICE
		Given the following rooms exist:
			| number | roomType | status      |
			| 521    | Single   | AVAILABLE   |
			| 522    | Double   | MAINTENANCE |
			| 523    | Single   | AVAILABLE   |
		When I filter rooms by status "OUT_OF_SERVICE"
		Then I should receive 1 rooms
		And all returned rooms should have status "OUT_OF_SERVICE"
