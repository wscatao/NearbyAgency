package br.com.agencies.nearbyagencies.domain.gateway;

import br.com.agencies.nearbyagencies.domain.Address;

public interface ZipCodeClientGateway {
    Address getAddress(String zipcode);
}
