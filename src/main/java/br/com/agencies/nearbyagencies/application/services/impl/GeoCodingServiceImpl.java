package br.com.agencies.nearbyagencies.application.services.impl;

import br.com.agencies.nearbyagencies.application.services.GeoCodingService;
import br.com.agencies.nearbyagencies.application.services.exception.GeoCodingApiException;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

    private final GeoApiContext geoApiContext;

    public GeoCodingServiceImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public GeocodingResult[] getGeocoding(String zipCode) {
        try {
            return GeocodingApi.geocode(geoApiContext, zipCode).await();
        } catch (ApiException | InterruptedException | IOException exception) {
            Thread.currentThread().interrupt();
            throw new GeoCodingApiException("Error searching for Address at Google Maps API");
        }
    }
}
