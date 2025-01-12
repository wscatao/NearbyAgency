package br.com.agencies.nearbyagencies.application.services.impl;

import br.com.agencies.nearbyagencies.application.services.GeoHashService;
import com.github.davidmoten.geo.GeoHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeoHashServiceImpl implements GeoHashService {

    private static final Logger logger = LoggerFactory.getLogger(GeoHashServiceImpl.class);

    @Override
    public String encodeGeoHash(double latitude, double longitude) {
        logger.info("Encoding GeoHash for latitude: {}, longitude: {}", latitude, longitude);

        String geoHash = GeoHash.encodeHash(latitude, longitude);

        logger.info("Encoded GeoHash: {}", geoHash);
        return geoHash;
    }
}
