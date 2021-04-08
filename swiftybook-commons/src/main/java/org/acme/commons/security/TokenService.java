package org.acme.commons.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.security.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Provider;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.BodyPublishers;

;

@Slf4j
@RequestScoped
public class TokenService {
    // We are using getting the property value as Provider<String> and not String to avoid failing the build of the native image
    // Gets the mp.jwt.verify.issuer property value to build the Keycloak Tokens Endpoint URL.
    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "undefined")
    Provider<String> jwtIssuerUrlProvider;

    @ConfigProperty(name = "keycloak.credentials.client-id", defaultValue = "undefined")
    Provider<String> clientIdProvider;

    // Custom metrics, increment each time the getAccessToken() method is invoked
    @Counted(name = "accessTokensRequestsCounter", description = "How many access_tokens have been requested.")

    // Custom metrics, create a time recorder for the duration of executing the getAccessToken() method.
    @Timed(name = "getAccessTokenRequestsTimer", description = "A measure of how long it takes to get an access_token.", unit = MetricUnits.MILLISECONDS)
    public String getAccessToken(String userName, String password) throws IOException, InterruptedException {
        return getAccessToken(jwtIssuerUrlProvider.get(), userName, password, clientIdProvider.get(), null);
    }

    public String getAccessToken(String jwtIssuerUrl, String userName, String password, String clientId, String clientSecret) throws IOException, InterruptedException {
        String tokenizer = jwtIssuerUrl + "/protocol/openid-connect/token";

        String requestBody = "username=" + userName + "&password=" + password + "&grant_type=password&client_id=" + clientId;

        if (clientSecret != null) {
            requestBody += "&client_secret=" + clientSecret;
        }

        // HTTP Client newly introduced in JAVA 11
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenizer))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String accessToken = "";

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                accessToken = mapper.readTree(response.body()).get("access_token").textValue();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.error("UnauthorizedException");
            throw new UnauthorizedException();
        }

        return accessToken;
    }
}
