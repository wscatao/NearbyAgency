package br.com.santander.nearagencyapi.application.services;

import com.google.maps.model.GeocodingResult;

public interface GeoCodingService {
    GeocodingResult[] geoCode(String address) ;
}
