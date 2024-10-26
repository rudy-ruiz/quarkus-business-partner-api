package org.antares.query;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.antares.dto.BusinessPartnerDTO;
import org.antares.dto.BusinessPartnerResponseDTO;
import org.antares.exceptions.BusinessPartnerNotFoundException;
import org.antares.model.BusinessPartner;
import org.antares.repository.BusinessPartnerRepository;
import org.modelmapper.ModelMapper;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BusinessPartnerQueryService {

    @Inject
    BusinessPartnerRepository businessPartnerRepository;

    @Inject
    ModelMapper modelMapper;

    /*
     * @WithSession
     * public Uni<BusinessPartnerResponseDTO> listAll() {
     * return businessPartnerRepository.listAll()
     * .onItem().transform(businessPartners -> {
     * BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
     * response.type = "success";
     * response.message = "Listando BusinessPartners";
     * response.status = "200";
     * response.datos = businessPartners.stream()
     * .map(persona -> modelMapper.map(persona, BusinessPartnerDTO.class))
     * .collect(Collectors.toList());
     * return response;
     * });
     * }
     * 
     * @WithSession
     * public Uni<BusinessPartnerResponseDTO> findById(Long id) {
     * return businessPartnerRepository.findById(id)
     * .onItem().ifNull().failWith(() -> new
     * BusinessPartnerNotFoundException(id.toString()))
     * .onItem().transform(businessPartner -> {
     * BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
     * response.type = "success";
     * response.message = "BusinessPartner encontrado";
     * response.status = "200";
     * if (businessPartner != null) {
     * response.datos = Collections
     * .singletonList(modelMapper.map(businessPartner, BusinessPartnerDTO.class));
     * } else {
     * response.datos = Collections.emptyList();
     * }
     * return response;
     * });
     * }
     * 
     */

    @WithSession
    public Uni<BusinessPartnerResponseDTO> listAll() {
        return businessPartnerRepository.listAll()
                .onItem().transform(this::createListAllResponse)
                .onFailure().recoverWithItem(this::createFailureResponse);
    }

    @WithSession
    public Uni<BusinessPartnerResponseDTO> findById(Long id) {
        return businessPartnerRepository.findById(id)
                .onItem().ifNull().failWith(() -> new BusinessPartnerNotFoundException(id.toString()))
                .onItem().transform(this::createFindByIdResponse)
                .onFailure(BusinessPartnerNotFoundException.class)
                .recoverWithItem(this::createNotFoundResponse)
                .onFailure().recoverWithItem(this::createFailureResponse);
    }

    private BusinessPartnerResponseDTO createListAllResponse(List<BusinessPartner> businessPartners) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "success";
        response.message = "Listando BusinessPartners";
        response.status = "200";
        response.datos = businessPartners.stream()
                .map(bp -> modelMapper.map(bp, BusinessPartnerDTO.class))
                .collect(Collectors.toList());
        return response;
    }

    private BusinessPartnerResponseDTO createFindByIdResponse(BusinessPartner businessPartner) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "success";
        response.message = "BusinessPartner encontrado";
        response.status = "200";
        response.datos = Collections.singletonList(modelMapper.map(businessPartner, BusinessPartnerDTO.class));
        return response;
    }

    private BusinessPartnerResponseDTO createNotFoundResponse(Throwable throwable) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "error";
        response.message = "BusinessPartner no encontrado";
        response.status = "404";
        response.datos = Collections.emptyList();
        return response;
    }

    private BusinessPartnerResponseDTO createFailureResponse(Throwable throwable) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "error";
        response.message = "Ocurrió un error: " + throwable.getMessage();
        response.status = "500";
        response.datos = Collections.emptyList();
        return response;
    }

}
