package com.daboerp.gestion.acceptance.steps;

import com.daboerp.gestion.acceptance.context.TestContext;
import com.daboerp.gestion.api.dto.CreateDocumentTypeRequest;
import com.daboerp.gestion.api.dto.DocumentTypeResponse;
import com.daboerp.gestion.api.dto.PaginatedResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentTypeManagementSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext testContext;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DOCUMENT_TYPES_API_URL = "/api/v1/document-types";

    private List<DocumentTypeResponse> documentTypeResponseList;
    private DocumentTypeResponse createdDocumentType;
    private DocumentTypeResponse retrievedDocumentType;
    private PaginatedResponse<DocumentTypeResponse> paginatedResponse;

    // Given steps
    @Given("the following document types exist in the system:")
    public void theFollowingDocumentTypesExistInTheSystem(DataTable dataTable) {
        List<Map<String, String>> documentTypes = dataTable.asMaps();
        
        // Create each document type via API
        for (Map<String, String> documentTypeData : documentTypes) {
            // Use default values if fields are not provided
            String description = documentTypeData.getOrDefault("description", "Test document type");
            String validationRegex = documentTypeData.getOrDefault("validationRegex", "^[A-Z0-9]+$");
            
            CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
                documentTypeData.get("code"),
                documentTypeData.get("name"), 
                description,
                validationRegex,
                Boolean.parseBoolean(documentTypeData.get("active"))
            );
            
            restTemplate.postForEntity(DOCUMENT_TYPES_API_URL, request, DocumentTypeResponse.class);
        }
    }

    @Given("a document type with code {string} already exists")
    public void aDocumentTypeWithCodeAlreadyExists(String code) {
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
            code,
            "Existing Type",
            "An existing document type",
            "^[A-Z0-9]+$",
            true
        );
        
        restTemplate.postForEntity(DOCUMENT_TYPES_API_URL, request, DocumentTypeResponse.class);
    }

    @Given("a document type exists with the following details:")
    public void aDocumentTypeExistsWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> docTypeData = dataTable.asMap();
        
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
            docTypeData.get("code"),
            docTypeData.get("name"),
            docTypeData.get("description"),
            docTypeData.get("validationRegex"),
            Boolean.parseBoolean(docTypeData.get("active"))
        );
        
        ResponseEntity<DocumentTypeResponse> response = restTemplate.postForEntity(
            DOCUMENT_TYPES_API_URL, 
            request, 
            DocumentTypeResponse.class
        );
        
        if (response.getStatusCode().is2xxSuccessful()) {
            createdDocumentType = response.getBody();
        }
    }

    @Given("there are {int} document types in the system")
    public void thereAreDocumentTypesInTheSystem(int count) {
        for (int i = 1; i <= count; i++) {
            CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
                "TYPE" + i,
                "Document Type " + i,
                "Description for type " + i,
                "^[A-Z0-9]{" + (i + 4) + "}$",
                true
            );
            
            restTemplate.postForEntity(DOCUMENT_TYPES_API_URL, request, DocumentTypeResponse.class);
        }
    }

    // When steps
    @When("I request all document types")
    public void iRequestAllDocumentTypes() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            DOCUMENT_TYPES_API_URL, 
            String.class
        );
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                documentTypeResponseList = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<DocumentTypeResponse>>() {}
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse document types response", e);
            }
        }
    }

    @When("I request only active document types")
    public void iRequestOnlyActiveDocumentTypes() {
        String url = UriComponentsBuilder.fromUriString(DOCUMENT_TYPES_API_URL)
            .queryParam("activeOnly", "true")
            .toUriString();
            
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                documentTypeResponseList = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<DocumentTypeResponse>>() {}
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse document types response", e);
            }
        }
    }

    @When("I create a document type with the following details:")
    public void iCreateADocumentTypeWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> docTypeData = dataTable.asMap();
        
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
            docTypeData.get("code"),
            docTypeData.get("name"),
            docTypeData.get("description"),
            docTypeData.get("validationRegex"),
            Boolean.parseBoolean(docTypeData.get("active"))
        );
        
        ResponseEntity<DocumentTypeResponse> response = restTemplate.postForEntity(
            DOCUMENT_TYPES_API_URL, 
            request, 
            DocumentTypeResponse.class
        );
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            createdDocumentType = response.getBody();
        }
    }

    @When("I create a new document type with code {string}")
    public void iCreateANewDocumentTypeWithCode(String code) {
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(
            code,
            "New Type",
            "A new document type",
            "^[A-Z0-9]+$",
            true
        );
        
        ResponseEntity<DocumentTypeResponse> response = restTemplate.postForEntity(
            DOCUMENT_TYPES_API_URL, 
            request, 
            DocumentTypeResponse.class
        );
        
        testContext.setLastResponse(response);
    }

    @When("I request the document type by code {string}")
    public void iRequestTheDocumentTypeByCode(String code) {
        String url = DOCUMENT_TYPES_API_URL + "/" + code;
        
        ResponseEntity<DocumentTypeResponse> response = restTemplate.getForEntity(
            url, 
            DocumentTypeResponse.class
        );
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            retrievedDocumentType = response.getBody();
        }
    }

    @When("I search for document types containing {string}")
    public void iSearchForDocumentTypesContaining(String searchTerm) {
        String url = UriComponentsBuilder.fromUriString(DOCUMENT_TYPES_API_URL + "/search")
            .queryParam("name", searchTerm)
            .toUriString();
            
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                documentTypeResponseList = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<List<DocumentTypeResponse>>() {}
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse search response", e);
            }
        }
    }

    @When("I request document types with page size {int} and page number {int}")
    public void iRequestDocumentTypesWithPageSizeAndPageNumber(int pageSize, int pageNumber) {
        String url = UriComponentsBuilder.fromUriString(DOCUMENT_TYPES_API_URL + "/paginated")
            .queryParam("page", pageNumber - 1) // Convert to 0-based
            .queryParam("size", pageSize)
            .toUriString();
            
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                paginatedResponse = objectMapper.readValue(
                    response.getBody(), 
                    new TypeReference<PaginatedResponse<DocumentTypeResponse>>() {}
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse paginated response", e);
            }
        }
    }

    // Then steps
    @Then("I should receive {int} document types")
    public void iShouldReceiveDocumentTypes(int expectedCount) {
        assertThat(testContext.getLastResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Handle paginated response
        if (paginatedResponse != null) {
            assertThat(paginatedResponse.content()).hasSize(expectedCount);
        } else {
            // Handle regular list response
            assertThat(documentTypeResponseList).hasSize(expectedCount);
        }
    }

    @Then("the document types should include:")
    public void theDocumentTypesShouldInclude(DataTable expectedTypes) {
        List<Map<String, String>> expected = expectedTypes.asMaps();
        
        assertThat(documentTypeResponseList).hasSize(expected.size());
        
        for (Map<String, String> expectedType : expected) {
            boolean found = documentTypeResponseList.stream()
                .anyMatch(dt -> {
                    boolean codeMatches = dt.code().equals(expectedType.get("code"));
                    boolean nameMatches = dt.name().equals(expectedType.get("name"));
                    
                    // Only check active field if it's provided in the data table
                    if (expectedType.containsKey("active")) {
                        boolean activeMatches = dt.active() == Boolean.parseBoolean(expectedType.get("active"));
                        return codeMatches && nameMatches && activeMatches;
                    }
                    
                    return codeMatches && nameMatches;
                });
            assertThat(found).as("Document type with code %s should be present", expectedType.get("code")).isTrue();
        }
    }

    @Then("each document type should have the required fields:")
    public void eachDocumentTypeShouldHaveTheRequiredFields(DataTable requiredFields) {
        List<String> fields = requiredFields.asList();
        
        for (DocumentTypeResponse docType : documentTypeResponseList) {
            if (fields.contains("code")) {
                assertThat(docType.code()).isNotNull().isNotBlank();
            }
            if (fields.contains("name")) {
                assertThat(docType.name()).isNotNull().isNotBlank();
            }
            if (fields.contains("description")) {
                assertThat(docType.description()).isNotNull();
            }
            if (fields.contains("validationRegex")) {
                // Can be null, just check it exists as a field
                // The getter should not throw an exception
                docType.validationRegex();
            }
            if (fields.contains("active")) {
                // Boolean field always has a value
                docType.active();
            }
        }
    }

    @Then("all returned document types should be active")
    public void allReturnedDocumentTypesShouldBeActive() {
        assertThat(documentTypeResponseList).allMatch(DocumentTypeResponse::active);
    }

    @Then("the active document types should be:")
    public void theActiveDocumentTypesShouldBe(DataTable expectedTypes) {
        List<Map<String, String>> expected = expectedTypes.asMaps();
        
        assertThat(documentTypeResponseList).hasSize(expected.size());
        
        for (Map<String, String> expectedType : expected) {
            boolean found = documentTypeResponseList.stream()
                .anyMatch(dt -> 
                    dt.code().equals(expectedType.get("code")) &&
                    dt.name().equals(expectedType.get("name")) &&
                    dt.active() // All should be active
                );
            assertThat(found).as("Active document type with code %s should be present", expectedType.get("code")).isTrue();
        }
    }

    @Then("the document type should be created successfully")
    public void theDocumentTypeShouldBeCreatedSuccessfully() {
        assertThat(testContext.getLastResponse().getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdDocumentType).isNotNull();
        assertThat(createdDocumentType.id()).isNotNull().isNotBlank();
    }

    @Then("the document type should have the code {string}")
    public void theDocumentTypeShouldHaveTheCode(String expectedCode) {
        assertThat(createdDocumentType.code()).isEqualTo(expectedCode);
    }

    @Then("the document type should be active")
    public void theDocumentTypeShouldBeActive() {
        assertThat(createdDocumentType.active()).isTrue();
    }

    @Then("the document type creation should fail")
    public void theDocumentTypeCreationShouldFail() {
        assertThat(testContext.getLastResponse().getStatusCode()).isIn(
            HttpStatus.BAD_REQUEST, 
            HttpStatus.CONFLICT
        );
    }

    @Then("I should receive an error about duplicate document type code")
    public void iShouldReceiveAnErrorAboutDuplicateDocumentTypeCode() {
        assertThat(testContext.getLastResponse().getStatusCode()).isIn(
            HttpStatus.BAD_REQUEST,
            HttpStatus.CONFLICT
        );
    }

    @Then("I should receive the document type details")
    public void iShouldReceiveTheDocumentTypeDetails() {
        assertThat(testContext.getLastResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(retrievedDocumentType).isNotNull();
    }

    @Then("the details should match the created document type")
    public void theDetailsShouldMatchTheCreatedDocumentType() {
        assertThat(retrievedDocumentType).isNotNull();
        assertThat(createdDocumentType).isNotNull();
        assertThat(retrievedDocumentType.code()).isEqualTo(createdDocumentType.code());
        assertThat(retrievedDocumentType.name()).isEqualTo(createdDocumentType.name());
        assertThat(retrievedDocumentType.description()).isEqualTo(createdDocumentType.description());
        assertThat(retrievedDocumentType.active()).isEqualTo(createdDocumentType.active());
    }

    @Then("the returned document types should be:")
    public void theReturnedDocumentTypesShouldBe(DataTable expectedTypes) {
        theDocumentTypesShouldInclude(expectedTypes);
    }

    @Then("the response should indicate page {int} of {int} total pages")
    public void theResponseShouldIndicatePageOfTotalPages(int expectedCurrentPage, int expectedTotalPages) {
        assertThat(paginatedResponse).isNotNull();
        assertThat(paginatedResponse.currentPage()).isEqualTo(expectedCurrentPage - 1); // Convert back to 0-based
        assertThat(paginatedResponse.totalPages()).isEqualTo(expectedTotalPages);
    }

    @Then("the response should include pagination metadata")
    public void theResponseShouldIncludePaginationMetadata() {
        assertThat(paginatedResponse).isNotNull();
        assertThat(paginatedResponse.pageSize()).isPositive();
        assertThat(paginatedResponse.totalElements()).isNotNegative();
        assertThat(paginatedResponse.totalPages()).isNotNegative();
    }
}