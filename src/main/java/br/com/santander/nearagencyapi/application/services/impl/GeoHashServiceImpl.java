package br.com.santander.nearagencyapi.application.services.impl;

import br.com.santander.nearagencyapi.application.services.GeoHashService;
import com.github.davidmoten.geo.GeoHash;
import org.springframework.stereotype.Service;

@Service
public class GeoHashServiceImpl implements GeoHashService {

    @Override
    public String encodeGeoHash(double latitude, double longitude) {
        return GeoHash.encodeHash(latitude, longitude);
    }
}
