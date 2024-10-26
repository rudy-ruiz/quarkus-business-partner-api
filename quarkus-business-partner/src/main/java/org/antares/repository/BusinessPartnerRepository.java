package org.antares.repository;

import org.antares.model.BusinessPartner;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BusinessPartnerRepository implements PanacheRepository<BusinessPartner> {

}
