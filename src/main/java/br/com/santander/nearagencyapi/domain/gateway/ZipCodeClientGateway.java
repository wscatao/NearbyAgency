package br.com.santander.nearagencyapi.domain.gateway;

import br.com.santander.nearagencyapi.domain.Address;

public interface ZipCodeClientGateway {
    Address getAddress(String zipcode);
}
