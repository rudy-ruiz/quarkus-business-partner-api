package org.antares.command;

import java.util.Collections;

import org.antares.dto.BusinessPartnerDTO;
import org.antares.dto.BusinessPartnerResponseDTO;
import org.antares.model.BusinessPartner;
import org.antares.repository.BusinessPartnerRepository;
import org.modelmapper.ModelMapper;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BusinessPartnerCommandService {

    @Inject
    BusinessPartnerRepository businessPartnerRepository;

    @Inject
    ModelMapper modelMapper;

    /*
     * @WithTransaction
     * public Uni<BusinessPartnerResponseDTO> save(BusinessPartnerDTO
     * businessPartnerDTO) {
     * BusinessPartner businessPartner = modelMapper.map(businessPartnerDTO,
     * BusinessPartner.class);
     * 
     * return businessPartnerRepository.persist(businessPartner)
     * .onItem().transform(persisted -> {
     * BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
     * response.type = "success";
     * response.message = "BusinessPartner creada con éxito";
     * response.status = "200";
     * response.datos = Collections.singletonList(modelMapper.map(persisted,
     * BusinessPartnerDTO.class));
     * return response;
     * });
     * }
     * 
     * @WithTransaction
     * public Uni<BusinessPartnerResponseDTO> update(Long id, BusinessPartnerDTO
     * businessPartnerDTO) {
     * return businessPartnerRepository.findById(id)
     * .onItem().ifNotNull()
     * .invoke(businessPartner -> {
     * businessPartner.setCardCode(businessPartnerDTO.getCardCode());
     * businessPartner.setCardName(businessPartnerDTO.getCardName());
     * })
     * .call(businessPartnerRepository::persist)
     * .onItem().transform(persisted -> {
     * BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
     * response.type = "success";
     * response.message = "BusinessPartner actualizada con éxito";
     * response.status = "200";
     * response.datos = Collections.singletonList(modelMapper.map(persisted,
     * BusinessPartnerDTO.class));
     * return response;
     * });
     * }
     * 
     * @WithTransaction
     * public Uni<BusinessPartnerResponseDTO> delete(Long id) {
     * return businessPartnerRepository.deleteById(id)
     * .onItem().transform(deleted -> {
     * BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
     * response.type = "success";
     * response.message = "BusinessPartner eliminada con éxito";
     * response.status = "200";
     * response.datos = Collections.emptyList();
     * return response;
     * });
     * }
     */

    @WithTransaction
    public Uni<BusinessPartnerResponseDTO> save(BusinessPartnerDTO businessPartnerDTO) {
        BusinessPartner businessPartner = modelMapper.map(businessPartnerDTO, BusinessPartner.class);

        return businessPartnerRepository.persist(businessPartner)
                .onItem().transform(persisted -> createSuccessResponse("BusinessPartner creada con éxito", persisted))
                .onFailure().recoverWithItem(this::createFailureResponse);
    }

    @WithTransaction
    public Uni<BusinessPartnerResponseDTO> update(Long id, BusinessPartnerDTO businessPartnerDTO) {
        return businessPartnerRepository.findById(id)
                .onItem().ifNotNull().invoke(businessPartner -> {
                    businessPartner.setCardCode(businessPartnerDTO.getCardCode());
                    businessPartner.setCardName(businessPartnerDTO.getCardName());
                })
                .call(businessPartnerRepository::persist)
                .onItem().ifNotNull()
                .transform(updated -> createSuccessResponse("BusinessPartner actualizada con éxito", updated))
                .onItem().ifNull().continueWith(this::createNotFoundResponse)
                .onFailure().recoverWithItem(this::createFailureResponse);
    }

    @WithTransaction
    public Uni<BusinessPartnerResponseDTO> delete(Long id) {
        return businessPartnerRepository.deleteById(id)
                .onItem().transform(deleted -> deleted
                        ? createSuccessResponse("BusinessPartner eliminada con éxito", null)
                        : createNotFoundResponse())
                .onFailure().recoverWithItem(this::createFailureResponse);
    }

    private BusinessPartnerResponseDTO createSuccessResponse(String message, BusinessPartner entity) {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "success";
        response.message = message;
        response.status = "200";
        response.datos = entity != null
                ? Collections.singletonList(modelMapper.map(entity, BusinessPartnerDTO.class))
                : Collections.emptyList();
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

    private BusinessPartnerResponseDTO createNotFoundResponse() {
        BusinessPartnerResponseDTO response = new BusinessPartnerResponseDTO();
        response.type = "error";
        response.message = "BusinessPartner no encontrado";
        response.status = "404";
        response.datos = Collections.emptyList();
        return response;
    }
}