package br.com.agencies.nearbyagencies.application.usecases.impl;

import br.com.agencies.nearbyagencies.application.services.GeoCodingService;
import br.com.agencies.nearbyagencies.application.services.GeoHashService;
import br.com.agencies.nearbyagencies.application.usecases.AgencyUseCases;
import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.domain.exception.AgencyNotFoundException;
import br.com.agencies.nearbyagencies.domain.exception.GeoCodingException;
import br.com.agencies.nearbyagencies.domain.exception.NearbyAgenciesException;
import br.com.agencies.nearbyagencies.domain.gateway.AgencyGateway;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

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
            GeocodingResult[] geocodingResults = geoCodingService.getGeocoding(agency.getAgencyZipCode());

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
    public Agency getAgency(String bankCode, String agencyNumber) {
        Optional<Agency> agency = agencyGateway.findByBankCodeAndAgencyNumber(bankCode, agencyNumber);
        if (agency.isEmpty()) {
            throw new AgencyNotFoundException("Agency not found or does not exist");
        } else {
            return agency.get();
        }
    }

    @Override
    public void updateAgency(Agency agency) {
        ensureAgencyExists(agency);
        agencyGateway.update(agency);
    }

    @Override
    public void deleteAgency(Agency agency) {
        ensureAgencyExists(agency);
        agencyGateway.delete(agency);
    }

    @Override
    public Page<Agency> getNearbyAgencies(String bankCode, String zipCode, Integer radius, String nextPageToken, int limit) {

        try {
            AtomicReference<String> geoHash = new AtomicReference<>();
            GeocodingResult[] geocodingResults = geoCodingService.getGeocoding(zipCode);

            Arrays.stream(geocodingResults).findFirst().ifPresent(geocodingResult ->
                    geoHash.set(geoHashService.encodeGeoHash(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)));

            String geoHashCode = geoHash.get();
            String geoHashPrefix = geoHashCode.substring(INTEGER_ZERO, radius);
            return agencyGateway.findByBankAndGeoHash(bankCode, geoHashPrefix, nextPageToken, limit);

        } catch (Exception exception) {
            throw new NearbyAgenciesException("Error getting nearby agencies");
        }
    }

    private void ensureAgencyExists(Agency agency) {
        Optional<Agency> optionalAgency = agencyGateway.findByBankCodeAndAgencyNumber(agency.getBank().getCode(), agency.getAgencyNumber());
        if (optionalAgency.isEmpty()) {
            throw new AgencyNotFoundException("Agency not found or does not exist");
        }
    }
}
