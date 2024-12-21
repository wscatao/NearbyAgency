package br.com.santander.nearagencyapi.domain;

import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
import br.com.santander.nearagencyapi.interfaces.dto.UpdateAgencyDto;
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
    private Long version;

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

    public static Agency toAgency(UpdateAgencyDto updateAgencyDto, String agencyZipCode, String agencyNumber, String ifMatch) {
        var agency = new Agency();
        agency.setAgencyZipCode(agencyZipCode);
        agency.setAgencyNumber(agencyNumber);
        agency.setAgencyName(updateAgencyDto.getAgencyName());
        agency.setAgencyTelephone(updateAgencyDto.getAgencyTelephone());
        agency.setAgencyEmail(updateAgencyDto.getAgencyEmail());
        agency.setServices(updateAgencyDto.getServices());
        agency.setVersion(Long.parseLong(ifMatch.replace("\"", "")));
        return agency;
    }
}
