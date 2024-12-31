package br.com.agencies.nearbyagencies.domain;

import br.com.agencies.nearbyagencies.domain.constant.Bank;
import br.com.agencies.nearbyagencies.interfaces.dto.CreateAgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.UpdateAgencyDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Agency {
    private String agencyZipCode;
    private Bank bank;
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

    public static Agency toAgency(CreateAgencyDto createAgencyDto) {
        var agency = new Agency();
        agency.setAgencyZipCode(createAgencyDto.getAgencyZipCode());
        agency.setBank(Bank.fromCode(createAgencyDto.getBankNumber()));
        agency.setAgencyNumber(createAgencyDto.getAgencyNumber());
        agency.setAgencyName(createAgencyDto.getAgencyName());
        agency.setAgencyTelephone(createAgencyDto.getAgencyTelephone());
        agency.setAgencyEmail(createAgencyDto.getAgencyEmail());
        agency.setAgencyAddress(createAgencyDto.getAgencyAddress());
        agency.setServices(createAgencyDto.getServices());
        return agency;
    }

    public static Agency toAgency(UpdateAgencyDto updateAgencyDto, String bankCode, String agencyNumber, String ifMatch) {
        var agency = new Agency();
        agency.setBank(Bank.fromCode(bankCode));
        agency.setAgencyNumber(agencyNumber);
        agency.setAgencyName(updateAgencyDto.getAgencyName());
        agency.setAgencyTelephone(updateAgencyDto.getAgencyTelephone());
        agency.setAgencyEmail(updateAgencyDto.getAgencyEmail());
        agency.setServices(updateAgencyDto.getServices());
        agency.setVersion(Long.parseLong(ifMatch.replace("\"", "")));
        return agency;
    }

    public static Agency toAgency(String bankCode, String agencyNumber) {
        var agency = new Agency();
        agency.setBank(Bank.fromCode(bankCode));
        agency.setAgencyNumber(agencyNumber);
        return agency;
    }
}
