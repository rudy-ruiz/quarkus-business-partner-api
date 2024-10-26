package org.antares.exceptions;

public class BusinessPartnerNotFoundException extends RuntimeException {

    public BusinessPartnerNotFoundException(String id) {
        super("Persona con ID " + id + " no encontrada");
    }
}
