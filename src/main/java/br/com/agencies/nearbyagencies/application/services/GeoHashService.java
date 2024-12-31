package br.com.agencies.nearbyagencies.application.services;

public interface GeoHashService {
    String encodeGeoHash(double latitude, double longitude);
}
