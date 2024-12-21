package br.com.santander.nearagencyapi.application.usecases;

import br.com.santander.nearagencyapi.domain.Agency;
import com.google.maps.errors.ApiException;

import java.io.IOException;

public interface AgencyUseCases {
    void createAgency(Agency agency) throws IOException, InterruptedException, ApiException;
    Agency getAgencyByCepAndNumber(String zipCode, String agencyNumber);
    void updateAgency(Agency agency);
}
