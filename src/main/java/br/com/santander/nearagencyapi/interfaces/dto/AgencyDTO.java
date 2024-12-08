package br.com.santander.nearagencyapi.interfaces.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AgencyDto {
    private String agencyZipCode;
    private String agencyNumber;
    private String agencyName;
    private String agencyTelephone;
    private String agencyEmail;
    private String agencyAddress;
    private List<String> services;
}
