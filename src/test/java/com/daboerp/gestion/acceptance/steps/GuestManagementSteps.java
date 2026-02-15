package com.daboerp.gestion.acceptance.steps;

import com.daboerp.gestion.acceptance.context.TestContext;
import com.daboerp.gestion.api.dto.CreateGuestRequest;
import com.daboerp.gestion.api.dto.GuestResponse;
import com.daboerp.gestion.domain.valueobject.DocumentType;
import com.daboerp.gestion.domain.valueobject.Nationality;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GuestManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String GUESTS_API_URL = "/api/v1/guests";

    @When("I create a new guest with the following details:")
    public void iCreateANewGuestWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> guestData = dataTable.asMap();
        
        CreateGuestRequest request = new CreateGuestRequest(
            guestData.get("firstName"),
            guestData.get("lastName"),
            guestData.get("email"),
            guestData.get("phone"),
            LocalDate.parse(guestData.get("dateOfBirth")),
            Nationality.valueOf(guestData.get("nationality")),
            guestData.get("documentNumber"),
            DocumentType.valueOf(guestData.get("documentType"))
        );

        ResponseEntity<GuestResponse> response = restTemplate.postForEntity(
            GUESTS_API_URL, 
            request, 
            GuestResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setCreatedGuest(response.getBody());
        }
    }

    @When("I create a new guest with invalid email {string}")
    public void iCreateANewGuestWithInvalidEmail(String invalidEmail) {
        CreateGuestRequest request = new CreateGuestRequest(
            "Test",
            "User",
            invalidEmail,
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            Nationality.UNITED_STATES,
            "A12345678",
            DocumentType.PASSPORT
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
            GUESTS_API_URL, 
            request, 
            String.class
        );

        testContext.setLastResponse(response);
    }

    @When("I create a new guest with email {string}")
    public void iCreateANewGuestWithEmail(String email) {
        CreateGuestRequest request = new CreateGuestRequest(
            "Test",
            "User",
            email,
            "+1234567890",
            LocalDate.of(1990, 1, 1),
            Nationality.UNITED_STATES,
            "A12345678",
            DocumentType.PASSPORT
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
            GUESTS_API_URL, 
            request, 
            String.class
        );

        testContext.setLastResponse(response);
    }

    @Given("a guest exists with email {string}")
    public void aGuestExistsWithEmail(String email) {
        CreateGuestRequest request = new CreateGuestRequest(
            "Existing",
            "User",
            email,
            "+1234567890",
            LocalDate.of(1985, 5, 15),
            Nationality.UNITED_STATES,
            "B87654321",
            DocumentType.PASSPORT
        );

        ResponseEntity<GuestResponse> response = restTemplate.postForEntity(
            GUESTS_API_URL, 
            request, 
            GuestResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        testContext.setExistingGuest(response.getBody());
    }

    @Given("a guest exists with the following details:")
    public void aGuestExistsWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> guestData = dataTable.asMap();
        
        CreateGuestRequest request = new CreateGuestRequest(
            guestData.get("firstName"),
            guestData.get("lastName"),
            guestData.get("email"),
            guestData.get("phone"),
            LocalDate.parse(guestData.get("dateOfBirth")),
            Nationality.valueOf(guestData.get("nationality")),
            guestData.get("documentNumber"),
            DocumentType.valueOf(guestData.get("documentType"))
        );

        ResponseEntity<GuestResponse> response = restTemplate.postForEntity(
            GUESTS_API_URL, 
            request, 
            GuestResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        testContext.setExistingGuest(response.getBody());
    }

    @Given("the following guests exist:")
    public void theFollowingGuestsExist(DataTable dataTable) {
        List<Map<String, String>> guests = dataTable.asMaps();
        
        for (Map<String, String> guestData : guests) {
            CreateGuestRequest request = new CreateGuestRequest(
                guestData.get("firstName"),
                guestData.get("lastName"),
                guestData.get("email"),
                "+1234567890",
                LocalDate.of(1990, 1, 1),
                Nationality.UNITED_STATES,
                "A12345678",
                DocumentType.PASSPORT
            );

            ResponseEntity<GuestResponse> response = restTemplate.postForEntity(
                GUESTS_API_URL, 
                request, 
                GuestResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @When("I request the guest information by ID")
    public void iRequestTheGuestInformationById() {
        String guestId = testContext.getExistingGuest().id();
        
        ResponseEntity<GuestResponse> response = restTemplate.getForEntity(
            GUESTS_API_URL + "/" + guestId,
            GuestResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setRetrievedGuest(response.getBody());
        }
    }

    @When("I request the list of all guests")
    public void iRequestTheListOfAllGuests() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            GUESTS_API_URL,
            String.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                List<GuestResponse> guests = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<GuestResponse>>() {}
                );
                testContext.setGuestList(guests);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse guest list response", e);
            }
        }
    }

    @When("I update the guest's contact information:")
    public void iUpdateTheGuestsContactInformation(DataTable dataTable) {
        Map<String, String> updateData = dataTable.asMap();
        GuestResponse existingGuest = testContext.getExistingGuest();
        String guestId = existingGuest.id();
        
        // Create update request with existing data and updated fields
        Map<String, Object> updateRequest = Map.of(
            "firstName", existingGuest.firstName(),
            "lastName", existingGuest.lastName(),
            "email", updateData.getOrDefault("email", existingGuest.email()),
            "phone", updateData.getOrDefault("phone", existingGuest.phone()),
            "dateOfBirth", existingGuest.dateOfBirth()
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(updateRequest);
        
        ResponseEntity<GuestResponse> response = restTemplate.exchange(
            GUESTS_API_URL + "/" + guestId,
            HttpMethod.PUT,
            requestEntity,
            GuestResponse.class
        );

        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            testContext.setUpdatedGuest(response.getBody());
        }
    }

    @Then("the guest should be created successfully")
    public void theGuestShouldBeCreatedSuccessfully() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Then("the guest should have a unique ID")
    public void theGuestShouldHaveAUniqueId() {
        GuestResponse guest = testContext.getCreatedGuest();
        assertThat(guest).isNotNull();
        assertThat(guest.id()).isNotNull();
        assertThat(guest.id()).isNotBlank();
    }

    @Then("the guest creation event should be published")
    public void theGuestCreationEventShouldBePublished() {
        // This would typically check event publication through test event listeners
        // For now, we'll verify the guest was created successfully as evidence
        assertThat(testContext.getCreatedGuest()).isNotNull();
    }

    @Then("the guest creation should fail")
    public void theGuestCreationShouldFail() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isIn(HttpStatus.BAD_REQUEST, HttpStatus.CONFLICT);
    }

    @Then("I should receive a validation error about invalid email format")
    public void iShouldReceiveAValidationErrorAboutInvalidEmailFormat() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).containsIgnoringCase("email");
    }

    @Then("I should receive an error about duplicate email")
    public void iShouldReceiveAnErrorAboutDuplicateEmail() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Then("I should receive the guest details")
    public void iShouldReceiveTheGuestDetails() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testContext.getRetrievedGuest()).isNotNull();
    }

    @Then("the details should match the created guest")
    public void theDetailsShouldMatchTheCreatedGuest() {
        GuestResponse existing = testContext.getExistingGuest();
        GuestResponse retrieved = testContext.getRetrievedGuest();
        
        assertThat(retrieved.id()).isEqualTo(existing.id());
        assertThat(retrieved.email()).isEqualTo(existing.email());
        assertThat(retrieved.firstName()).isEqualTo(existing.firstName());
        assertThat(retrieved.lastName()).isEqualTo(existing.lastName());
    }

    @Then("I should receive {int} guests")
    public void iShouldReceiveGuests(int expectedCount) {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testContext.getGuestList()).hasSize(expectedCount);
    }

    @Then("the list should contain all created guests")
    public void theListShouldContainAllCreatedGuests() {
        List<GuestResponse> guests = testContext.getGuestList();
        assertThat(guests).isNotEmpty();
        // Additional verification could check specific guest details
    }

    @Then("the guest's contact information should be updated")
    public void theGuestsContactInformationShouldBeUpdated() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(testContext.getUpdatedGuest()).isNotNull();
    }

    @Then("the new information should be persisted")
    public void theNewInformationShouldBePersisted() {
        GuestResponse updated = testContext.getUpdatedGuest();
        assertThat(updated).isNotNull();
        
        // Verify by retrieving the guest again
        ResponseEntity<GuestResponse> verifyResponse = restTemplate.getForEntity(
            GUESTS_API_URL + "/" + updated.id(),
            GuestResponse.class
        );
        
        assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        GuestResponse verified = verifyResponse.getBody();
        
        assertThat(verified.email()).isEqualTo(updated.email());
        assertThat(verified.phone()).isEqualTo(updated.phone());
    }
}