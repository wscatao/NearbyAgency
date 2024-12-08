package br.com.santander.nearagencyapi.infrastructure.adapters.persistence;

import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.domain.gateway.AgencyGateway;
import br.com.santander.nearagencyapi.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgencyPersistenceAdapterImpl implements AgencyGateway {

    private final DynamoDbTemplate dynamoDbTemplate;

    public AgencyPersistenceAdapterImpl(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public void save(Agency agency) {
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setAgencyNumber(agency.getAgencyNumber());
        agencyModel.setAgencyName(agency.getAgencyName());
        agencyModel.setAgencyTelephone(agency.getAgencyTelephone());
        agencyModel.setAgencyEmail(agency.getAgencyEmail());
        agencyModel.setAgencyAddress(agency.getAgencyAddress());
        agencyModel.setServices(agency.getServices());
        agencyModel.setFormattedAddress(agency.getFormattedAddress());
        agencyModel.setGeoHash(agency.getGeoHash());
        agencyModel.setLatitude(agency.getLatitude());
        agencyModel.setLongitude(agency.getLongitude());
        dynamoDbTemplate.save(agencyModel);
    }
}
