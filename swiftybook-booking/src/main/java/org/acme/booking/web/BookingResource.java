package org.acme.booking.web;

import io.quarkus.security.Authenticated;
import org.acme.booking.service.BookingService;
import org.acme.commons.dto.BookingDto;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//@Authenticated
@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "booking", description = "All the booking methods")
public class BookingResource {

    @Inject
    BookingService bookingService;

//    @RolesAllowed("admin")
    @GET
    public List<BookingDto> findAll() {
        return this.bookingService.findAll();
    }

    @GET
    @Path("/customer/{id}")
    public List<BookingDto> findAllByUser(@PathParam("id") Long id) {
        return this.bookingService.findAllByUser(id);
    }

    @GET
    @Path("/{id}")
    public BookingDto findById(@PathParam("id") Long id) {
        return this.bookingService.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public BookingDto create(BookingDto bookingDto) {
        return this.bookingService.create(bookingDto);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.bookingService.delete(id);
    }

    @GET
    @Path("/exists/{id}")
    public boolean existsById(@PathParam("id") Long id) {
        return this.bookingService.existsById(id);
    }

    @GET
    @Path("/payment/{id}")
    public BookingDto findByPaymentId(@PathParam("id") Long id) {
        return this.bookingService.findByPaymentId(id);
    }
}
