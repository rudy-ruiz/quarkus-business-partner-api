package org.antares.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessPartnerDTO {

    @NotBlank
    private String cardCode;
    @NotBlank
    private String cardName;

}
