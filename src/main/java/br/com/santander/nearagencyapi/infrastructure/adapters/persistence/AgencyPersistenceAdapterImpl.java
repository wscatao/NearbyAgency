package br.com.santander.nearagencyapi.infrastructure.adapters.persistence;

import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.domain.gateway.AgencyGateway;
import br.com.santander.nearagencyapi.infrastructure.adapters.exception.OptimisticLockingException;
import br.com.santander.nearagencyapi.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.util.Optional;

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

    @Override
    public Optional<Agency> findByCepAndNumber(String zipCode, String agencyNumber) {

        Key key = Key.builder()
                .partitionValue(zipCode)
                .sortValue(agencyNumber)
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if (agencyModel == null) {
            return Optional.empty();
        }

        Agency agency = new Agency();
        agency.setAgencyZipCode(agencyModel.getAgencyZipCode());
        agency.setAgencyNumber(agencyModel.getAgencyNumber());
        agency.setAgencyName(agencyModel.getAgencyName());
        agency.setAgencyTelephone(agencyModel.getAgencyTelephone());
        agency.setAgencyEmail(agencyModel.getAgencyEmail());
        agency.setAgencyAddress(agencyModel.getAgencyAddress());
        agency.setServices(agencyModel.getServices());
        agency.setFormattedAddress(agencyModel.getFormattedAddress());
        agency.setGeoHash(agencyModel.getGeoHash());
        agency.setLatitude(agencyModel.getLatitude());
        agency.setLongitude(agencyModel.getLongitude());
        agency.setVersion(agencyModel.getVersion());

        return Optional.of(agency);
    }

    @Override
    public void update(Agency agency) {

        Key key = Key.builder()
                .partitionValue(agency.getAgencyZipCode())
                .sortValue(agency.getAgencyNumber())
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if(agencyModel != null) {
            agencyModel.setAgencyName(agency.getAgencyName());
            agencyModel.setAgencyTelephone(agency.getAgencyTelephone());
            agencyModel.setAgencyEmail(agency.getAgencyEmail());
            agencyModel.setServices(agency.getServices());
            agencyModel.setVersion(agency.getVersion());

            try {
                dynamoDbTemplate.update(agencyModel);
            } catch (ConditionalCheckFailedException e) {
                throw new OptimisticLockingException("Version mismatch - update failed");
            }
        }
    }
}
