package br.com.agencies.nearbyagencies.application.services;

import com.google.maps.model.GeocodingResult;

public interface GeoCodingService {
    GeocodingResult[] getGeocoding(String zipCode) ;
}
