package org.acme.commons.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.ws.rs.core.Application;

@SecurityScheme(
        securitySchemeName = "JWT",
        description = "JWT authentication with bearer token", // description for the OpenAPI Definition.
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "Bearer [token]"
)
// Linking the created security schema jwt to the OpenAPI Definition
@OpenAPIDefinition(
        info = @Info(
                title = "SwiftyBooking, API",
                description = "SwiftyBooking, yet another booking system",
                contact = @Contact(name = "Mohamed Awad", email = "mohamed.awad@mycit.ie", url = "https://mgalalm.github.io"),
                version = "1.0.0-SNAPSHOT"
        ),
        security = @SecurityRequirement(name = "JWT")
)
public class OpenApiConfig extends Application {

}
