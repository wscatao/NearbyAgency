package br.com.santander.nearagencyapi.factory;

import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;

import java.util.List;

public class AgencyFactory {

    public static Agency createDefaultAgency() {
        Agency agency = new Agency();
        agency.setAgencyZipCode("09111410");
        agency.setAgencyNumber("2783");
        agency.setAgencyName("Sto André Av. São Paulo");
        agency.setAgencyTelephone("114004-4828");
        agency.setAgencyEmail("2783@itau.com.br");
        agency.setAgencyAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agency.setServices(List.of(
                "Abertura de contas",
                "Pagamentos",
                "Negócios",
                "Atendimento Varejo",
                "Atendimento Empresas",
                "Acessibilidade"
        ));
        return agency;
    }

    public static AgencyDto createDefaultAgencyDto() {
        AgencyDto agencyDto = new AgencyDto();
        agencyDto.setAgencyZipCode("09111410");
        agencyDto.setAgencyNumber("2783");
        agencyDto.setAgencyName("Sto André Av. São Paulo");
        agencyDto.setAgencyTelephone("114004-4828");
        agencyDto.setAgencyEmail("2783@itau.com.br");
        agencyDto.setAgencyAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agencyDto.setServices(List.of(
                "Abertura de contas",
                "Pagamentos",
                "Negócios",
                "Atendimento Varejo",
                "Atendimento Empresas",
                "Acessibilidade"
        ));
        return agencyDto;
    }

    public static AgencyDto createWithInvalidZipCode() {
        AgencyDto agencyDto = createDefaultAgencyDto();
        agencyDto.setAgencyZipCode("123"); // Invalid zip code
        return agencyDto;
    }

    public static AgencyDto createWithInvalidEmail() {
        AgencyDto agencyDto = createDefaultAgencyDto();
        agencyDto.setAgencyEmail("invalid-email"); // Invalid email
        return agencyDto;
    }

    public static AgencyDto createWithMissingFields() {
        AgencyDto agencyDto = new AgencyDto();
        return agencyDto;
    }
}
