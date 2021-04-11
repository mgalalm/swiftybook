package org.acme.customer.web;

import org.acme.commons.dto.PaymentDto;
import org.acme.customer.service.PaymentService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

//@Authenticated
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "payment", description = "All the payment methods")
public class PaymentResource {

    @Inject
    PaymentService paymentService;

//    @RolesAllowed("admin")
    @GET
    public List<PaymentDto> findAll() {
        return this.paymentService.findAll();
    }

    @GET
    @Path("/{id}")
    public PaymentDto findById(@PathParam("id") Long id) {
        return this.paymentService.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public PaymentDto create(PaymentDto orderItemDto) {
        return this.paymentService.create(orderItemDto);
    }

    @RolesAllowed("admin")
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.paymentService.delete(id);
    }

    @GET
    @Path("/price/{price}")
    public List<PaymentDto> findPaymentsByAmountRangeMax(@PathParam("price") double max) {
        return this.paymentService.findByPriceRange(max);
    }
}
