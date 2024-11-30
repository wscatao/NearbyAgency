package br.com.santander.nearagencyapi.infrastructure.adapters.restclient;

import br.com.santander.nearagencyapi.domain.Address;
import br.com.santander.nearagencyapi.domain.gateway.ZipCodeClientGateway;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ZipCodeClient implements ZipCodeClientGateway {

    private final RestClient restClient;

    public ZipCodeClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Address getAddress(String zipcode) {


        return restClient.get()
                .uri("ws/{zipcode}/json/", zipcode)
                .retrieve()
                .body(Address.class);
    }
}
