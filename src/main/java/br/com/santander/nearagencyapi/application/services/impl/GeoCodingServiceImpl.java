package br.com.santander.nearagencyapi.application.services.impl;

import br.com.santander.nearagencyapi.application.services.GeoCodingService;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

@Service
public class GeoCodingServiceImpl implements GeoCodingService {

    private final GeoApiContext geoApiContext;

    public GeoCodingServiceImpl(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @Override
    public GeocodingResult[] geoCode(String address) {

        try {
            return GeocodingApi.geocode(geoApiContext, address).await();
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao buscar endereço", exception);
        }
    }
}
