package br.com.santander.nearagencyapi.application.usecases.impl;

import br.com.santander.nearagencyapi.application.services.GeoCodingService;
import br.com.santander.nearagencyapi.application.services.GeoHashService;
import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.domain.exception.AgencyNotFoundException;
import br.com.santander.nearagencyapi.domain.exception.GeoCodingException;
import br.com.santander.nearagencyapi.domain.gateway.AgencyGateway;
import br.com.santander.nearagencyapi.factory.AgencyFactory;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AgencyUseCasesImplTest {

    @Mock
    private GeoCodingService geoCodingService;

    @Mock
    private AgencyGateway agencyGateway;

    @Mock
    private GeoHashService geoHashService;

    @InjectMocks
    private AgencyUseCasesImpl agencyUseCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAgency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        GeocodingResult geocodingResult = new GeocodingResult();
        geocodingResult.geometry = new Geometry();
        geocodingResult.geometry.location = new LatLng(37.7749, -122.4194);
        geocodingResult.formattedAddress = "Formatted Address";

        when(geoCodingService.geoCode(anyString())).thenReturn(new GeocodingResult[]{geocodingResult});
        when(geoHashService.encodeGeoHash(anyDouble(), anyDouble())).thenReturn("geoHash");

        agencyUseCases.createAgency(agency);

        ArgumentCaptor<Agency> agencyCaptor = ArgumentCaptor.forClass(Agency.class);
        verify(agencyGateway).save(agencyCaptor.capture());

        Agency savedAgency = agencyCaptor.getValue();
        assertEquals(37.7749, savedAgency.getLatitude());
        assertEquals(-122.4194, savedAgency.getLongitude());
        assertEquals("Formatted Address", savedAgency.getFormattedAddress());
        assertEquals("geoHash", savedAgency.getGeoHash());
    }

    @Test
    void createAgency_geoCodingException() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(geoCodingService.geoCode(anyString())).thenThrow(new RuntimeException("GeoCoding error"));

        assertThrows(GeoCodingException.class, () -> agencyUseCases.createAgency(agency));
    }

    @Test
    void getAgencyByCepAndNumber_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByCepAndNumber("09111410", "2783")).thenReturn(Optional.of(agency));

        Agency result = agencyUseCases.getAgencyByCepAndNumber("09111410", "2783");

        assertEquals(agency, result);
    }

    @Test
    void getAgencyByCepAndNumber_notFound() {
        when(agencyGateway.findByCepAndNumber("09111410", "2783")).thenReturn(Optional.empty());

        assertThrows(AgencyNotFoundException.class, () -> agencyUseCases.getAgencyByCepAndNumber("09111410", "2783"));
    }

    @Test
    void updateAgency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByCepAndNumber("09111410", "2783")).thenReturn(Optional.of(agency));

        agencyUseCases.updateAgency(agency);

        verify(agencyGateway).update(agency);
    }

    @Test
    void updateAgency_notFound() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByCepAndNumber("09111410", "2783")).thenReturn(Optional.empty());

        assertThrows(AgencyNotFoundException.class, () -> agencyUseCases.updateAgency(agency));
    }
}
