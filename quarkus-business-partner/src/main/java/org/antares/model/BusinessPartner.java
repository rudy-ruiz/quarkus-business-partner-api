package org.antares.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name = "clientes2")
public class BusinessPartner{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    public String cardCode;

    @NotBlank(message = "El nombre no puede estar vacío")
    public String cardName;
}
