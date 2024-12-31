package br.com.agencies.nearbyagencies.interfaces.dto;

import br.com.agencies.nearbyagencies.domain.Agency;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AgencyDto {

    private String agencyZipCode;

    private String bank;

    private String agencyNumber;

    private String agencyName;

    private String agencyTelephone;

    private String agencyEmail;

    private String agencyAddress;

    private List<String> services;

    public static AgencyDto fromAgency(Agency agency) {
        AgencyDto createAgencyDto = new AgencyDto();
        createAgencyDto.setAgencyZipCode(agency.getAgencyZipCode());
        createAgencyDto.setAgencyNumber(agency.getAgencyNumber());
        createAgencyDto.setBank(agency.getBank().getName());
        createAgencyDto.setAgencyName(agency.getAgencyName());
        createAgencyDto.setAgencyTelephone(agency.getAgencyTelephone());
        createAgencyDto.setAgencyEmail(agency.getAgencyEmail());
        createAgencyDto.setAgencyAddress(agency.getAgencyAddress());
        createAgencyDto.setServices(agency.getServices());
        return createAgencyDto;
    }
}
