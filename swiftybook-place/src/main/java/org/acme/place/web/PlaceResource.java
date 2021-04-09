package org.acme.place.web;

import org.acme.commons.dto.PlaceDto;
import org.acme.place.service.PlaceService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/places")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "place", description = "All the place methods")
public class PlaceResource {

    @Inject
    PlaceService placeService;

    @GET
    public List<PlaceDto> findAll() {
        return this.placeService.findAll();
    }

    @GET
    @Path("/count")
    public Long countAllPlaces() {
        return this.placeService.countAll();
    }

    @GET
    @Path("/{id}")
    public PlaceDto findById(@PathParam("id") Long id) {
        return this.placeService.findById(id);
    }

    @RolesAllowed("admin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public PlaceDto create(PlaceDto placeDto) {
        return this.placeService.create(placeDto);
    }

    @RolesAllowed("admin")
    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        this.placeService.delete(id);
    }

    @GET
    @Path("/category/{id}")
    public List<PlaceDto> findByCategoryId(@PathParam("id") Long id) {
        return this.placeService.findByCategoryId(id);
    }

    @GET
    @Path("/count/category/{id}")
    public Long countByCategoryId(@PathParam("id") Long id) {
        return this.placeService.countByCategoryId(id);
    }
}
