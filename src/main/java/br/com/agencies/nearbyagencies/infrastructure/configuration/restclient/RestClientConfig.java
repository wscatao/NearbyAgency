package br.com.agencies.nearbyagencies.infrastructure.configuration.restclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Profile("local")
    public RestClient localRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8081/viacep.com.br/")
                .build();
    }

    @Bean
    @Profile("!local")
    public RestClient defaultRestClient() {
        return RestClient.builder()
                .baseUrl("https://viacep.com.br/")
                .build();
    }
}
