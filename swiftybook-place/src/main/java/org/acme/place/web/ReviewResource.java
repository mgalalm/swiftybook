package org.acme.place.web;

import io.quarkus.security.Authenticated;
import org.acme.commons.dto.ReviewDto;
import org.acme.place.service.ReviewService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "review", description = "All the review methods")
public class ReviewResource {

    @Inject
    ReviewService reviewService;

    @GET
    @Path("/place/{id}")
    public List<ReviewDto> findAllByPlace(@PathParam("id") Long id) {
        return this.reviewService.findReviewsByPlaceId(id);
    }

    @GET
    @Path("/{id}")
    public ReviewDto findById(@PathParam("id") Long id) {
        return this.reviewService.findById(id);
    }

    @Authenticated
    @POST
    @Path("/place/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ReviewDto create(ReviewDto reviewDto, @PathParam("id") Long id) {
        return this.reviewService.create(reviewDto, id);
    }

    @RolesAllowed("admin")
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.reviewService.delete(id);
    }
}
