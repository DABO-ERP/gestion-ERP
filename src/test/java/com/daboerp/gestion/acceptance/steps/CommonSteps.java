package com.daboerp.gestion.acceptance.steps;

import com.daboerp.gestion.acceptance.context.TestContext;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Common step definitions shared across all feature files.
 * Contains setup and background steps.
 */
public class CommonSteps {

    @Autowired
    private TestContext testContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        // Clear test context before each scenario
        testContext.clearAll();
    }

    @Given("the system is running")
    public void theSystemIsRunning() {
        // Verify the application is running on the expected port
        // This step is more for documentation purposes in Gherkin
        // The actual verification is handled by Spring Boot Test
    }

    @Given("the database is clean")
    public void theDatabaseIsClean() {
        // Clean tables in correct order to handle foreign key constraints
        jdbcTemplate.execute("DELETE FROM reservation_guests");
        jdbcTemplate.execute("DELETE FROM reservations");
        jdbcTemplate.execute("DELETE FROM beds");
        jdbcTemplate.execute("DELETE FROM room_amenities");
        jdbcTemplate.execute("DELETE FROM rooms");
        jdbcTemplate.execute("DELETE FROM room_types");
        jdbcTemplate.execute("DELETE FROM guests");
        jdbcTemplate.execute("DELETE FROM document_types");
    }
}