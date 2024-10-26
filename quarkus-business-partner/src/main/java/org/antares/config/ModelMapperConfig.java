package org.antares.config;

import org.modelmapper.ModelMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ModelMapperConfig {

    @Produces
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}