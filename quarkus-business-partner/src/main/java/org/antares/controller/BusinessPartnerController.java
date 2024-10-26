package org.antares.controller;

import java.util.Collections;

import org.antares.command.BusinessPartnerCommandService;
import org.antares.dto.BusinessPartnerDTO;
import org.antares.dto.BusinessPartnerResponseDTO;
import org.antares.exceptions.BusinessPartnerNotFoundException;
import org.antares.query.BusinessPartnerQueryService;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/BusinessPartner")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BusinessPartnerController {

    @Inject
    BusinessPartnerCommandService businessPartnerCommandService;

    @Inject
    BusinessPartnerQueryService businessPartnerQueryService;

    /*
     * @GET
     * public Uni<BusinessPartnerResponseDTO> listAll() {
     * return businessPartnerQueryService.listAll();
     * }
     * 
     * @GET
     * 
     * @Path("/{id}")
     * public Uni<BusinessPartnerResponseDTO> findById(@PathParam("id") Long id) {
     * return personaQueryService.findById(id);
     * }
     * 
     * @POST
     * public Uni<BusinessPartnerResponseDTO> save(@Valid BusinessPartnerDTO
     * businessPartnerDTO) {
     * return personaCommandService.save(businessPartnerDTO);
     * }
     * 
     * @PUT
     * 
     * @Path("/{id}")
     * public Uni<BusinessPartnerResponseDTO> update(@PathParam("id") Long id,
     * 
     * @Valid BusinessPartnerDTO businessPartnerDTO) {
     * return personaCommandService.update(id, businessPartnerDTO);
     * }
     * 
     * @DELETE
     * 
     * @Path("/{id}")
     * public Uni<BusinessPartnerResponseDTO> delete(@PathParam("id") Long id) {
     * return personaCommandService.delete(id);
     * }
     */
    //JDK 21
    @GET
    public Uni<BusinessPartnerResponseDTO> listAll() {
        return businessPartnerQueryService.listAll()
                .onItem().transform(response -> {
                    response.status = "200";
                    return response;
                });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> findById(@PathParam("id") Long id) {
        return businessPartnerQueryService.findById(id)
                .onItem().transform(response -> Response.ok(response).build())
                .onFailure(BusinessPartnerNotFoundException.class)
                .recoverWithItem(() -> Response.status(Response.Status.NOT_FOUND).entity(createNotFoundResponse(id)).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(createErrorResponse(throwable)).build());
    }

    @POST
    public Uni<Response> save(@Valid BusinessPartnerDTO businessPartnerDTO) {
        return businessPartnerCommandService.save(businessPartnerDTO)
                .onItem().transform(response -> Response.status(Response.Status.CREATED).entity(response).build())
                .onFailure().recoverWithItem(throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(createErrorResponse(throwable)).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id") Long id, @Valid BusinessPartnerDTO businessPartnerDTO) {
        return businessPartnerCommandService.update(id, businessPartnerDTO)
                .onItem().transform(response -> Response.ok(response).build())
                .onFailure(BusinessPartnerNotFoundException.class).recoverWithItem(
                        () -> Response.status(Response.Status.NOT_FOUND).entity(createNotFoundResponse(id)).build())
                .onFailure().recoverWithItem(
                        throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(createErrorResponse(throwable)).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return businessPartnerCommandService.delete(id)
                .onItem().transform(response -> Response.noContent().build())
                .onFailure(BusinessPartnerNotFoundException.class).recoverWithItem(
                        () -> Response.status(Response.Status.NOT_FOUND).entity(createNotFoundResponse(id)).build())
                .onFailure().recoverWithItem(
                        throwable -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                .entity(createErrorResponse(throwable)).build());
    }

    private BusinessPartnerResponseDTO createNotFoundResponse(Long id) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "error";
        response.message = "BusinessPartner no encontrado con ID: " + id;
        response.status = "404";
        response.datos = Collections.emptyList();
        return response;
    }

    private BusinessPartnerResponseDTO createErrorResponse(Throwable throwable) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "error";
        response.message = "Ocurrió un error: " + throwable.getMessage();
        response.status = "500";
        response.datos = Collections.emptyList();
        return response;
    }
}