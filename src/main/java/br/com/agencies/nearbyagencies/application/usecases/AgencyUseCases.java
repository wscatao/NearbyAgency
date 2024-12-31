package br.com.agencies.nearbyagencies.application.usecases;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import com.google.maps.errors.ApiException;

import java.io.IOException;

public interface AgencyUseCases {
    void createAgency(Agency agency) throws IOException, InterruptedException, ApiException;
    Agency getAgency(String bankCode, String agencyNumber);
    void updateAgency(Agency agency);
    void deleteAgency(Agency agency);
    Page<Agency> getNearbyAgencies(String bankCode, String zipCode, Integer radius, String nextPageToken, int limit);
}
