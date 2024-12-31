package br.com.agencies.nearbyagencies.interfaces.dto;

import br.com.agencies.nearbyagencies.domain.Agency;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateAgencyDto {

    @NotBlank(message = "bank number is required")
    @Pattern(regexp = "^\\d{3}$", message = "bank number must have 3 digits")
    private String bankNumber;

    @NotBlank(message = "agency number is required")
    @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
    private String agencyNumber;

    @NotBlank(message = "zip code is required")
    @Pattern(regexp = "^\\d{8}$", message = "zip code must have 8 digits")
    private String agencyZipCode;

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

    @NotNull(message = "The services list cannot be null")
    private List<String> services;

    public static CreateAgencyDto fromAgency(Agency agency) {
        CreateAgencyDto createAgencyDto = new CreateAgencyDto();
        createAgencyDto.setAgencyZipCode(agency.getAgencyZipCode());
        createAgencyDto.setAgencyNumber(agency.getAgencyNumber());
        createAgencyDto.setAgencyName(agency.getAgencyName());
        createAgencyDto.setAgencyTelephone(agency.getAgencyTelephone());
        createAgencyDto.setAgencyEmail(agency.getAgencyEmail());
        createAgencyDto.setAgencyAddress(agency.getAgencyAddress());
        createAgencyDto.setServices(agency.getServices());
        return createAgencyDto;
    }
}
