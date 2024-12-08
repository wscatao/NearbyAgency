package br.com.santander.nearagencyapi.application.services;

public interface GeoHashService {
    String encodeGeoHash(double latitude, double longitude);
}
