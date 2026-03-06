package com.daboerp.gestion.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestionErpOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Development server");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.daboerp.com");
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setEmail("info@daboerp.com");
        contact.setName("DABO ERP Team");
        contact.setUrl("https://www.daboerp.com");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Gestion ERP API")
                .version("1.0.0")
                .contact(contact)
                .description("RESTful API for Hostel/Hotel Management System - Core Management Features")
                .termsOfService("https://www.daboerp.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
