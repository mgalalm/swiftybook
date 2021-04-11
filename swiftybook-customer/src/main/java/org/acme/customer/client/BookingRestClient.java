package org.acme.customer.client;

import org.acme.commons.dto.BookingDto;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/bookings")
@ApplicationScoped
@RegisterRestClient  // For CDI injection as a REST Client
public interface BookingRestClient {
    @GET
    @Path("/{id}")
    @CircuitBreaker(requestVolumeThreshold = 10, delay = 15000)
    @Retry(maxRetries = 4)
    @Timeout(500)
    Optional<BookingDto>findById(@PathParam Long id);


    @GET
    @Path("/payment/{id}")
    @CircuitBreaker(requestVolumeThreshold = 10, delay = 15000)
    @Retry(maxRetries = 4)
    @Timeout(500)
    Optional<BookingDto> findByPaymentId(@PathParam("id") Long id);

    @POST
    @CircuitBreaker(requestVolumeThreshold = 10, delay = 15000)
    @Retry(maxRetries = 4)
    @Timeout(500)
    Optional<BookingDto> save(BookingDto booking);
}
