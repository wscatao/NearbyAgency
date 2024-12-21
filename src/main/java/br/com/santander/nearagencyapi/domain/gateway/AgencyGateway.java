package br.com.santander.nearagencyapi.domain.gateway;

import br.com.santander.nearagencyapi.domain.Agency;

import java.util.Optional;

public interface AgencyGateway {

    void save(Agency agency);
    Optional<Agency> findByCepAndNumber(String zipCode, String agencyNumber);
    void update(Agency agency);
}
