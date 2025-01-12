package br.com.agencies.nearbyagencies.application.services.impl;

import br.com.agencies.nearbyagencies.application.services.GeoCodingService;
import br.com.agencies.nearbyagencies.application.services.exception.GeoCodingApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

    private static final Logger logger = LoggerFactory.getLogger(GeoCodingServiceImpl.class);
    private final GeoApiContext geoApiContext;

    public GeoCodingServiceImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public GeocodingResult[] getGeocoding(String zipCode) {
        logger.info("Fetching geocoding for zipCode: {}", zipCode);

        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, zipCode).await();

            logger.info("Geocoding results found for zipCode: {}", zipCode);

            return results;
        } catch (ApiException | InterruptedException | IOException exception) {
            logger.error("Error fetching geocoding for zipCode: {}", zipCode, exception);
            Thread.currentThread().interrupt();
            throw new GeoCodingApiException("Error searching for Address at Google Maps API");
        }
    }
}
