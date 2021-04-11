package org.acme.customer.client;

import org.acme.commons.dto.BookingDto;
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
    Optional<BookingDto>findById(@PathParam Long id);


    @GET
    @Path("/payment/{id}")
    Optional<BookingDto> findByPaymentId(@PathParam("id") Long id);

    @POST
    Optional<BookingDto> save(BookingDto booking);
}
