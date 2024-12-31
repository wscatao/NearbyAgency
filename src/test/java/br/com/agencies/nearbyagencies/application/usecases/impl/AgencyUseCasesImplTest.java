package br.com.agencies.nearbyagencies.application.usecases.impl;

import br.com.agencies.nearbyagencies.application.services.GeoCodingService;
import br.com.agencies.nearbyagencies.application.services.GeoHashService;
import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.domain.exception.AgencyNotFoundException;
import br.com.agencies.nearbyagencies.domain.exception.GeoCodingException;
import br.com.agencies.nearbyagencies.domain.exception.NearbyAgenciesException;
import br.com.agencies.nearbyagencies.domain.gateway.AgencyGateway;
import br.com.agencies.nearbyagencies.factory.AgencyFactory;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
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

        when(geoCodingService.getGeocoding(anyString())).thenReturn(new GeocodingResult[]{geocodingResult});
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

        when(geoCodingService.getGeocoding(anyString())).thenThrow(new RuntimeException("GeoCoding error"));

        assertThrows(GeoCodingException.class, () -> agencyUseCases.createAgency(agency));
    }

    @Test
    void getAgency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.of(agency));

        Agency result = agencyUseCases.getAgency("341", "2783");

        assertEquals(agency, result);
    }

    @Test
    void getAgency_notFound() {
        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.empty());

        assertThrows(AgencyNotFoundException.class, () -> agencyUseCases.getAgency("09111410", "2783"));
    }

    @Test
    void updateAgency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.of(agency));

        agencyUseCases.updateAgency(agency);

        verify(agencyGateway).update(agency);
    }

    @Test
    void updateAgency_notFound() {
        Agency agency = AgencyFactory.createDefaultAgency();

        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.empty());

        assertThrows(AgencyNotFoundException.class, () -> agencyUseCases.updateAgency(agency));
    }

    @Test
    void deleteAgency_success() {
        Agency agency = AgencyFactory.createDefaultAgency();
        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.of(agency));

        agencyUseCases.deleteAgency(agency);

        verify(agencyGateway, times(1)).delete(agency);
    }

    @Test
    void deleteAgency_notFound() {
        Agency agency = AgencyFactory.createDefaultAgency();
        when(agencyGateway.findByBankCodeAndAgencyNumber("341", "2783")).thenReturn(Optional.empty());

        assertThrows(AgencyNotFoundException.class, () -> agencyUseCases.deleteAgency(agency));
    }

    @Test
    void getNearbyAgencies_success() throws Exception {
        String bankCode = "341";
        String zipCode = "12345";
        int radius = 5;
        String nextPageToken = "token";
        int limit = 10;

        GeocodingResult geocodingResult = new GeocodingResult();
        geocodingResult.geometry = new Geometry();
        geocodingResult.geometry.location = new LatLng(37.7749, -122.4194);

        when(geoCodingService.getGeocoding(anyString())).thenReturn(new GeocodingResult[]{geocodingResult});
        when(geoHashService.encodeGeoHash(anyDouble(), anyDouble())).thenReturn("geoHash");
        when(agencyGateway.findByBankAndGeoHash(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(new Page<>(Collections.emptyList(), null));

        Page<Agency> result = agencyUseCases.getNearbyAgencies(bankCode, zipCode, radius, nextPageToken, limit);

        assertNotNull(result);
        verify(geoCodingService).getGeocoding(zipCode);
        verify(geoHashService).encodeGeoHash(37.7749, -122.4194);
        verify(agencyGateway).findByBankAndGeoHash(bankCode, "geoHash".substring(0, radius), nextPageToken, limit);
    }

    @Test
    void getNearbyAgencies_geoCodingException() {
        String bankCode = "341";
        String zipCode = "12345";
        int radius = 5;
        String nextPageToken = "token";
        int limit = 10;

        when(geoCodingService.getGeocoding(anyString())).thenThrow(new RuntimeException("GeoCoding error"));

        assertThrows(NearbyAgenciesException.class, () -> agencyUseCases.getNearbyAgencies(bankCode, zipCode, radius, nextPageToken, limit));
    }
}
