package org.acme.booking.client;

import org.acme.commons.dto.PlaceDto;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/places")
@RegisterRestClient  // For CDI injection as a REST Client
public interface PlaceRestClient {
    //Stop making the API Calls for 15 seconds, if we have a 50% of
    //failing request from the last 10 requests
    @GET
    @Path("/{id}")
    @CircuitBreaker(requestVolumeThreshold = 10, delay = 15000)
    @Retry(maxRetries = 4) // will make up to 4 retries in case of the invocation fails
    @Timeout(500) // will make the application throw a TimeoutException
    Optional<PlaceDto> findById(@PathParam Long id);
}
