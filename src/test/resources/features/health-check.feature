Feature: Health Check
  As a system administrator
  I want to verify the system is operational
  So that I can ensure basic functionality works

  @health-check
  Scenario: Application health check
    Given the system is running
    When I check the application health
    Then the system should be healthy