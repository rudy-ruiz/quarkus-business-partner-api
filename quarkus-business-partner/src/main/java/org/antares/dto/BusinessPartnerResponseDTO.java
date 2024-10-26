package org.antares.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessPartnerResponseDTO {

   public String type;
   public String message;
   public String status;
   public List< BusinessPartnerDTO> datos;

}
