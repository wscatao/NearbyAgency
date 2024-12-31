package br.com.agencies.nearbyagencies.infrastructure.configuration.geoapicontext;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GeoApiContextConfig {

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_TIMEOUT = 1000; // Timeout of 1 second between retries

    @Bean
    public GeoApiContext geoApiContext(@Value("${geoapicontext.api.key}") String apiKey) {
        return new GeoApiContext.Builder()
                .maxRetries(MAX_RETRIES)
                .retryTimeout(RETRY_TIMEOUT, TimeUnit.MILLISECONDS)
                .apiKey(apiKey)
                .build();
    }
}
