package br.com.agencies.nearbyagencies.interfaces.dto;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.factory.AgencyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateAgencyDtoTest {

    @Test
    void testFromAgency() {
        Agency agency = AgencyFactory.createDefaultAgency();
        CreateAgencyDto createAgencyDto = CreateAgencyDto.fromAgency(agency);

        assertEquals(agency.getAgencyZipCode(), createAgencyDto.getAgencyZipCode());
        assertEquals(agency.getAgencyNumber(), createAgencyDto.getAgencyNumber());
        assertEquals(agency.getAgencyName(), createAgencyDto.getAgencyName());
        assertEquals(agency.getAgencyTelephone(), createAgencyDto.getAgencyTelephone());
        assertEquals(agency.getAgencyEmail(), createAgencyDto.getAgencyEmail());
        assertEquals(agency.getAgencyAddress(), createAgencyDto.getAgencyAddress());
        assertEquals(agency.getServices(), createAgencyDto.getServices());
    }

}
