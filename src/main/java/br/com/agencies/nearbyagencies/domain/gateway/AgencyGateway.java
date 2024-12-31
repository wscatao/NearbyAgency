package br.com.agencies.nearbyagencies.domain.gateway;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;

import java.util.Optional;

public interface AgencyGateway {

    void save(Agency agency);
    Optional<Agency> findByBankCodeAndAgencyNumber(String bankCode, String agencyNumber);
    void update(Agency agency);
    void delete(Agency agency);
    Page<Agency> findByBankAndGeoHash(String bankCode, String geoHash, String nextPageToken, int limit) throws Exception;
}
