package org.acme.commons.health;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Provider;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Liveness
@ApplicationScoped
public class KeycloakConnectionHealthCheck implements HealthCheck {
    // mp.jwt.verify.publickey.location property, which we will use as the Keycloak URL.
    @ConfigProperty(name = "mp.jwt.verify.publickey.location", defaultValue = "false")
    Provider<String> keycloakUrl;

    @Override
    public HealthCheckResponse call() {
        // define the name of the Health Check as Keycloak connection health check.
        HealthCheckResponseBuilder responseBuilder =
                HealthCheckResponse.named("Keycloak connection health check");

        try {
            // verify that the Keycloak URL is reachable and the response status code is HTTP 200
            keycloakConnectionVerification();
            responseBuilder.up();
        } catch (IllegalStateException e) {
            // cannot access keycloak
            responseBuilder.down().withData("error", e.getMessage());
        }

        return responseBuilder.build();
    }

    private void keycloakConnectionVerification() {
        // instantiate the Java 11 HTTPClient with 3000 milliseconds of Timeout
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(3000))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(keycloakUrl.get()))
                .build();

        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            log.error("IOException", e);
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
            Thread.currentThread().interrupt();
        }
        if (response == null || response.statusCode() != 200) {
            throw new IllegalStateException("Cannot contact Keycloak");
        }
    }
}
