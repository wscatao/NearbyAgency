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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class AgencyUseCasesImpl implements AgencyUseCases {

    private static final Logger logger = LoggerFactory.getLogger(AgencyUseCasesImpl.class);
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

        logger.info("Creating agency with bankCode: {}", agency.getBank().getCode());

        try {
            GeocodingResult[] geocodingResults = geoCodingService.getGeocoding(agency.getAgencyZipCode());

            Arrays.stream(geocodingResults).findFirst().ifPresent(geocodingResult -> {
                agency.setLatitude(geocodingResult.geometry.location.lat);
                agency.setLongitude(geocodingResult.geometry.location.lng);
                agency.setFormattedAddress(geocodingResult.formattedAddress);
                agency.setGeoHash(geoHashService.encodeGeoHash(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng));
            });

            agencyGateway.save(agency);

            logger.info("Agency created with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        } catch (Exception exception) {
            logger.error("Error creating agency with bankCode: {}", agency.getBank().getCode(), exception);
            throw new GeoCodingException("Error getting geocoding data");
        }
    }

    @Override
    public Agency getAgency(String bankCode, String agencyNumber) {
        logger.info("Fetching agency with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);

        Optional<Agency> agency = agencyGateway.findByBankCodeAndAgencyNumber(bankCode, agencyNumber);
        if (agency.isEmpty()) {
            logger.warn("Agency not found with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);
            throw new AgencyNotFoundException("Agency not found or does not exist");
        } else {
            logger.info("Agency found with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);
            return agency.get();
        }
    }

    @Override
    public void updateAgency(Agency agency) {
        logger.info("Updating agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        ensureAgencyExists(agency);
        agencyGateway.update(agency);

        logger.info("Agency updated with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
    }

    @Override
    public void deleteAgency(Agency agency) {
        logger.info("Deleting agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        ensureAgencyExists(agency);
        agencyGateway.delete(agency);

        logger.info("Agency deleted with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
    }

    @Override
    public Page<Agency> getNearbyAgencies(String bankCode, String zipCode, Integer radius, String nextPageToken, int limit) {
        logger.info("Fetching nearby agencies with bankCode: {}, zipCode: {}, radius: {}, nextPageToken: {}, limit: {}", bankCode, zipCode, radius, nextPageToken, limit);

        try {
            AtomicReference<String> geoHash = new AtomicReference<>();
            GeocodingResult[] geocodingResults = geoCodingService.getGeocoding(zipCode);

            Arrays.stream(geocodingResults).findFirst().ifPresent(geocodingResult ->
                    geoHash.set(geoHashService.encodeGeoHash(geocodingResult.geometry.location.lat, geocodingResult.geometry.location.lng)));

            String geoHashCode = geoHash.get();
            String geoHashPrefix = geoHashCode.substring(0, radius);
            Page<Agency> nearbyAgencies = agencyGateway.findByBankAndGeoHash(bankCode, geoHashPrefix, nextPageToken, limit);

            logger.info("Found {} nearby agencies", nearbyAgencies.getItems().size());

            return nearbyAgencies;
        } catch (Exception exception) {
            logger.error("Error fetching nearby agencies with bankCode: {}, zipCode: {}", bankCode, zipCode, exception);
            throw new NearbyAgenciesException("Error getting nearby agencies");
        }
    }

    private void ensureAgencyExists(Agency agency) {
        logger.info("Ensuring agency exists with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        Optional<Agency> optionalAgency = agencyGateway.findByBankCodeAndAgencyNumber(agency.getBank().getCode(), agency.getAgencyNumber());
        if (optionalAgency.isEmpty()) {
            logger.warn("Agency not found with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
            throw new AgencyNotFoundException("Agency not found or does not exist");
        }
    }
}
