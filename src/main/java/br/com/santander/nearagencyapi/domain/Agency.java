package br.com.santander.nearagencyapi.domain;

import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Agency {
    private String agencyZipCode;
    private String agencyNumber;
    private String agencyName;
    private String agencyTelephone;
    private String agencyEmail;
    private String agencyAddress;
    private List<String> services;
    private String formattedAddress;
    private String geoHash;
    private double latitude;
    private double longitude;

    public static Agency toAgency(AgencyDto agencyDto) {
        var agency = new Agency();
        agency.setAgencyZipCode(agencyDto.getAgencyZipCode());
        agency.setAgencyNumber(agencyDto.getAgencyNumber());
        agency.setAgencyName(agencyDto.getAgencyName());
        agency.setAgencyTelephone(agencyDto.getAgencyTelephone());
        agency.setAgencyEmail(agencyDto.getAgencyEmail());
        agency.setAgencyAddress(agencyDto.getAgencyAddress());
        agency.setServices(agencyDto.getServices());
        return agency;
    }
}
