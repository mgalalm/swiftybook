package org.acme.booking.client;

import org.acme.commons.dto.PlaceDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/places")
@RegisterRestClient  // For CDI injection as a REST Client
public interface PlaceRestClient {
    @GET
    @Path("/{id}")
    Optional<PlaceDto> findById(@PathParam Long id);
}
