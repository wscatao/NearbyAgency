package br.com.santander.nearagencyapi.application.usecases.impl;

import br.com.santander.nearagencyapi.application.services.GeoCodingService;
import br.com.santander.nearagencyapi.application.services.GeoHashService;
import br.com.santander.nearagencyapi.application.usecases.AgencyUseCases;
import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.domain.exception.AgencyNotFoundException;
import br.com.santander.nearagencyapi.domain.exception.GeoCodingException;
import br.com.santander.nearagencyapi.domain.gateway.AgencyGateway;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class AgencyUseCasesImpl implements AgencyUseCases {

    private final GeoCodingService geoCodingService;
    private final AgencyGateway agencyGateway;
    private final GeoHashService geoHashService;

    public AgencyUseCasesImpl(AgencyGateway agencyGateway, GeoCodingService geoCodingService, GeoHashService geoHashService) {
        this.geoCodingService = geoCodingService;
        this.agencyGateway = agencyGateway;
        this.geoHashService = geoHashService;
    }

    @Override
    public void createAgency(Agency agency) {

        try {
            GeocodingResult[] geocodingResults = geoCodingService.geoCode(agency.getAgencyAddress());

            Arrays.stream(geocodingResults).findFirst().ifPresent(geocodingResult -> {
                agency.setLatitude(geocodingResult.geometry.location.lat);
                agency.setLongitude(geocodingResult.geometry.location.lng);
                agency.setFormattedAddress(geocodingResult.formattedAddress);
                agency.setGeoHash(geoHashService.encodeGeoHash(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng));
            });

            agencyGateway.save(agency);
        } catch (Exception exception) {
            throw new GeoCodingException("Error getting geocoding data");
        }
    }

    @Override
    public Agency getAgencyByCepAndNumber(String zipCode, String agencyNumber) {
        Optional<Agency> agency = agencyGateway.findByCepAndNumber(zipCode, agencyNumber);
        if (agency.isEmpty()) {
            throw new AgencyNotFoundException("Agency not found or does not exist");
        } else {
            return agency.get();
        }
    }
}
