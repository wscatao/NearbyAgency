package br.com.agencies.nearbyagencies.factory;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.domain.constant.Bank;
import br.com.agencies.nearbyagencies.infrastructure.model.AgencyModel;
import br.com.agencies.nearbyagencies.interfaces.dto.CreateAgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.UpdateAgencyDto;

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
        agency.setFormattedAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agency.setGeoHash("dr5ru7z");
        agency.setLatitude(-23.673253);
        agency.setLongitude(-46.543600);
        agency.setVersion(1L);
        agency.setBank(Bank.ITAU);
        return agency;
    }

    public static AgencyModel createDefaultAgencyModel() {
        AgencyModel agency = new AgencyModel();
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
        agency.setFormattedAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        agency.setGeoHash("dr5ru7z");
        agency.setLatitude(-23.673253);
        agency.setLongitude(-46.543600);
        agency.setVersion(1L);
        agency.setBankCode("341");
        return agency;
    }


    public static CreateAgencyDto createDefaultAgencyDto() {
        CreateAgencyDto createAgencyDto = new CreateAgencyDto();
        createAgencyDto.setBankNumber("341");
        createAgencyDto.setAgencyZipCode("09111410");
        createAgencyDto.setAgencyNumber("2783");
        createAgencyDto.setAgencyName("Sto André Av. São Paulo");
        createAgencyDto.setAgencyTelephone("114004-4828");
        createAgencyDto.setAgencyEmail("2783@itau.com.br");
        createAgencyDto.setAgencyAddress("Av. São Paulo, 250 - Cidade São Jorge, Santo André - SP, 09111-410");
        createAgencyDto.setServices(List.of(
                "Abertura de contas",
                "Pagamentos",
                "Negócios",
                "Atendimento Varejo",
                "Atendimento Empresas",
                "Acessibilidade"
        ));
        return createAgencyDto;
    }

    public static CreateAgencyDto createWithInvalidZipCode() {
        CreateAgencyDto createAgencyDto = createDefaultAgencyDto();
        createAgencyDto.setAgencyZipCode("123"); // Invalid zip code
        return createAgencyDto;
    }

    public static CreateAgencyDto createWithInvalidEmail() {
        CreateAgencyDto createAgencyDto = createDefaultAgencyDto();
        createAgencyDto.setAgencyEmail("invalid-email"); // Invalid email
        return createAgencyDto;
    }

    public static CreateAgencyDto createWithMissingFields() {
        return new CreateAgencyDto();
    }

    public static UpdateAgencyDto createDefaultUpdateAgencyDto() {
        UpdateAgencyDto updateAgencyDto = new UpdateAgencyDto();
        updateAgencyDto.setAgencyName("Name");
        updateAgencyDto.setAgencyTelephone("1140044829");
        updateAgencyDto.setAgencyEmail("abc@gmail.com");
        updateAgencyDto.setServices(List.of("Service 1", "Service 2"));
        return updateAgencyDto;
    }
}
