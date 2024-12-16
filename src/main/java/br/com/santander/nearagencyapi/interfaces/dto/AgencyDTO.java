package br.com.santander.nearagencyapi.interfaces.dto;

import br.com.santander.nearagencyapi.domain.Agency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AgencyDto {

    @NotBlank(message = "zip code is required")
    @Pattern(regexp = "^\\d{8}$", message = "zip code must have 8 digits")
    private String agencyZipCode;

    @NotBlank(message = "agency number is required")
    @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
    private String agencyNumber;

    @NotBlank(message = "agency name is required")
    private String agencyName;

    @Size(max = 15, message = "The agency telephone must have a maximum of 15 characters")
    @NotBlank(message = "The agency telephone is required")
    private String agencyTelephone;

    @Email(message = "Invalid email")
    @NotBlank(message = "The agency email is required")
    private String agencyEmail;

    @NotBlank(message = "The agency address is required")
    private String agencyAddress;

    private List<String> services;

    public static AgencyDto fromAgency(Agency agency) {
        AgencyDto agencyDto = new AgencyDto();
        agencyDto.setAgencyZipCode(agency.getAgencyZipCode());
        agencyDto.setAgencyNumber(agency.getAgencyNumber());
        agencyDto.setAgencyName(agency.getAgencyName());
        agencyDto.setAgencyTelephone(agency.getAgencyTelephone());
        agencyDto.setAgencyEmail(agency.getAgencyEmail());
        agencyDto.setAgencyAddress(agency.getAgencyAddress());
        agencyDto.setServices(agency.getServices());
        return agencyDto;
    }
}
