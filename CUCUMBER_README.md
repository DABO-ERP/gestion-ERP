# Cucumber Acceptance Testing

This project uses Cucumber for acceptance testing with Spring Boot integration. The tests are written in Gherkin syntax and provide end-to-end testing of the hospitality management system.

## Structure

```
src/test/
├── java/com/daboerp/gestion/acceptance/
│   ├── AcceptanceTestRunner.java          # Main test runner
│   ├── config/
│   │   └── CucumberSpringConfiguration.java # Spring configuration for tests
│   ├── context/
│   │   └── TestContext.java               # Shared test state
│   └── steps/
│       ├── CommonSteps.java               # Common step definitions
│       ├── GuestManagementSteps.java      # Guest management steps
│       ├── ReservationManagementSteps.java # Reservation management steps
│       └── RoomManagementSteps.java       # Room management steps
└── resources/
    ├── features/                          # Gherkin feature files
    │   ├── guest-management.feature
    │   ├── reservation-management.feature
    │   └── room-management.feature
    ├── sql/
    │   ├── cleanup.sql                    # Database cleanup script
    │   └── test-schema.sql               # Test database schema
    └── application-test.yml               # Test configuration
```

## Features

### Guest Management
- **guest-management.feature**: Tests for creating, retrieving, updating, and listing guests
- Scenarios covered:
  - Register new guest successfully
  - Handle invalid email validation
  - Prevent duplicate guest registration
  - Retrieve guest information
  - List all guests
  - Update guest contact information

### Reservation Management  
- **reservation-management.feature**: Tests for reservation lifecycle
- Scenarios covered:
  - Create new reservations
  - Validate reservation dates
  - Handle guest and room availability
  - Check-in process
  - Check-out process
  - List reservations by date range
  - Pricing strategy application

### Room Management
- **room-management.feature**: Tests for room and room type management
- Scenarios covered:
  - Create room types
  - Create rooms
  - List available rooms
  - Search rooms by capacity
  - Room status management
  - Room availability by date range

## Running Tests

### Run All Acceptance Tests
```bash
./gradlew test --tests "com.daboerp.gestion.acceptance.AcceptanceTestRunner"
```

### Run Specific Feature
You can use Cucumber tags to run specific scenarios:
```bash
./gradlew test -Dcucumber.filter.tags="@guest-management"
```

### Run with Reports
The tests generate reports in multiple formats:
- HTML report: `target/cucumber-reports/` 
- JSON report: `target/cucumber-reports/Cucumber.json`
- JUnit XML: `target/cucumber-reports/Cucumber.xml`

## Writing New Tests

### 1. Add Feature File
Create a new `.feature` file in `src/test/resources/features/`:

```gherkin
Feature: New Feature
  As a [role]
  I want to [action]
  So that [benefit]

  Background:
    Given the system is running
    And the database is clean

  Scenario: Test scenario name
    Given [precondition]
    When [action]
    Then [expected result]
```

### 2. Implement Step Definitions
Create or update step definition classes in `src/test/java/com/daboerp/gestion/acceptance/steps/`:

```java
@Component
public class NewFeatureSteps {
    
    @Autowired
    private TestContext testContext;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Given("some precondition")
    public void somePrecondition() {
        // Implementation
    }
    
    @When("some action")
    public void someAction() {
        // Implementation
    }
    
    @Then("expected result")
    public void expectedResult() {
        // Assertions using AssertJ
    }
}
```

### 3. Update Test Context
If you need to share data between steps, add it to `TestContext.java`:

```java
public class TestContext {
    private YourResponse createdEntity;
    
    public void setCreatedEntity(YourResponse entity) {
        this.createdEntity = entity;
    }
    
    public YourResponse getCreatedEntity() {
        return createdEntity;
    }
}
```

## Configuration

### Test Configuration Files
- **application-test.yml**: Spring Boot test configuration
- **test-schema.sql**: Database schema for testing  
- **cleanup.sql**: Database cleanup between scenarios

### Spring Integration
The tests use `@SpringBootTest` with random port and H2 in-memory database:
- Automatic Spring context loading
- Test transaction management
- Database cleanup between scenarios
- HTTP client configuration

## Best Practices

### Feature Files
1. **Business Language**: Use domain-specific language that business stakeholders understand
2. **Scenario Structure**: Follow Given-When-Then pattern consistently
3. **Background**: Use Background for common setup across scenarios
4. **Data Tables**: Use tables for complex test data

### Step Definitions
1. **Single Responsibility**: Each step should do one thing
2. **Reusability**: Create reusable steps across features
3. **Clear Assertions**: Use descriptive assertion messages
4. **Test Isolation**: Ensure scenarios don't depend on each other

### Test Data
1. **Clean State**: Each scenario starts with clean database
2. **Minimal Data**: Only create data needed for the test
3. **Realistic Data**: Use realistic test data that matches domain rules

## Debugging

### Enable Debug Logging
Add to `application-test.yml`:
```yaml
logging.level.com.daboerp: DEBUG
logging.level.org.springframework.web: DEBUG
```

### Failed Test Investigation
1. Check HTML report for detailed failure information
2. Review test logs for HTTP request/response details
3. Use `@Sql` annotations for database state verification
4. Add breakpoints in step definitions for debugging

## Integration with CI/CD

The tests are designed to run in CI environments:
- No external dependencies (uses H2 database)
- Configurable through environment variables
- Produces standard JUnit XML reports
- Fast execution suitable for build pipelines

## Dependencies

Key testing dependencies added:
- `io.cucumber:cucumber-java` - Cucumber for Java
- `io.cucumber:cucumber-spring` - Spring integration
- `io.cucumber:cucumber-junit-platform-engine` - JUnit 5 integration
- `org.junit.platform:junit-platform-suite` - Test suite support