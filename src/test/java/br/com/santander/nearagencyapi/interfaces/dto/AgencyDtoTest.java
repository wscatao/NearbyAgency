package br.com.santander.nearagencyapi.interfaces.dto;

import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.factory.AgencyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgencyDtoTest {

    @Test
    void testFromAgency() {
        Agency agency = AgencyFactory.createDefaultAgency();
        AgencyDto agencyDto = AgencyDto.fromAgency(agency);

        assertEquals(agency.getAgencyZipCode(), agencyDto.getAgencyZipCode());
        assertEquals(agency.getAgencyNumber(), agencyDto.getAgencyNumber());
        assertEquals(agency.getAgencyName(), agencyDto.getAgencyName());
        assertEquals(agency.getAgencyTelephone(), agencyDto.getAgencyTelephone());
        assertEquals(agency.getAgencyEmail(), agencyDto.getAgencyEmail());
        assertEquals(agency.getAgencyAddress(), agencyDto.getAgencyAddress());
        assertEquals(agency.getServices(), agencyDto.getServices());
    }

}
