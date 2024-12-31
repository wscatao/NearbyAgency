package br.com.agencies.nearbyagencies.infrastructure.adapters.persistence;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.domain.constant.Bank;
import br.com.agencies.nearbyagencies.domain.gateway.AgencyGateway;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.OptimisticLockingException;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.PaginationException;
import br.com.agencies.nearbyagencies.infrastructure.model.AgencyModel;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import br.com.agencies.nearbyagencies.infrastructure.util.PaginationUtils;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AgencyPersistenceAdapterImpl implements AgencyGateway {

    private final DynamoDbTemplate dynamoDbTemplate;

    public AgencyPersistenceAdapterImpl(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public void save(Agency agency) {
        AgencyModel agencyModel = toAgencyModel(agency);
        dynamoDbTemplate.save(agencyModel);
    }

    @Override
    public Optional<Agency> findByBankCodeAndAgencyNumber(String bankCode, String agencyNumber) {

        Key key = Key.builder()
                .partitionValue(bankCode)
                .sortValue(agencyNumber)
                .build();

        return Optional.ofNullable(dynamoDbTemplate.load(key, AgencyModel.class))
                .map(this::toAgency);
    }

    @Override
    public void update(Agency agency) {

        Key key = Key.builder()
                .partitionValue(agency.getBank().getCode())
                .sortValue(agency.getAgencyNumber())
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if (agencyModel != null) {
            updateAgencyModel(agency, agencyModel);
            try {
                dynamoDbTemplate.update(agencyModel);
            } catch (ConditionalCheckFailedException e) {
                throw new OptimisticLockingException("Version mismatch - update failed");
            }
        }
    }

    @Override
    public void delete(Agency agency) {

        Key key = Key.builder()
                .partitionValue(agency.getBank().getCode())
                .sortValue(agency.getAgencyNumber())
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if (agencyModel != null) {
            try {
                dynamoDbTemplate.delete(agencyModel);
            } catch (ConditionalCheckFailedException e) {
                throw new OptimisticLockingException("Version mismatch - delete failed");
            }
        }
    }

    @Override
    public Page<Agency> findByBankAndGeoHash(String bankCode, String geoHashPrefix, String nextPageToken, int limit) {

        Key key = Key.builder()
                .partitionValue(bankCode)
                .build();

        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);

        Expression filterExpression = Expression.builder()
                .expression("begins_with(geohash_code, :geoHashPrefix)")
                .putExpressionValue(":geoHashPrefix", AttributeValue.builder().s(geoHashPrefix).build())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .filterExpression(filterExpression)
                .limit(limit);

        if (nextPageToken != null && !nextPageToken.isEmpty()) {
            Map<String, AttributeValue> exclusiveStartKey = PaginationUtils.decodeBase64ToMap(nextPageToken);
            requestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        return dynamoDbTemplate.query(request, AgencyModel.class)
                .stream().findFirst().map(page -> {
                    Map<String, AttributeValue> stringAttributeValueMap = page.lastEvaluatedKey();
                    List<AgencyModel> itens = page.items().stream().limit(limit).toList();
                    return createPaginatedList(itens, stringAttributeValueMap, limit);
                }).orElse(new Page<>(List.of(), null));
    }

    private Page<Agency> createPaginatedList(List<AgencyModel> itens, Map<String, AttributeValue> stringAttributeValueMap, int limit) {
        try {
            String nextPageReference = null;

            if (stringAttributeValueMap != null && !stringAttributeValueMap.isEmpty()) {
                Map<String, String> lastEvaluatedKeyValue = new HashMap<>();

                for (Map.Entry<String, AttributeValue> entry : stringAttributeValueMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue().s();
                    lastEvaluatedKeyValue.put(key, value);
                }

                nextPageReference = PaginationUtils.encodeMapToBase64(lastEvaluatedKeyValue);
            }

            List<Agency> agencies = itens.stream().limit(limit).map(this::toAgency).toList();

            return new Page<>(agencies, nextPageReference);

        } catch (Exception e) {
            throw new PaginationException("Error creating paginated list");
        }
    }

    private AgencyModel toAgencyModel(Agency agency) {
        AgencyModel agencyModel = new AgencyModel();
        agencyModel.setAgencyZipCode(agency.getAgencyZipCode());
        agencyModel.setBankCode(agency.getBank().getCode());
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
        return agencyModel;
    }

    private Agency toAgency(AgencyModel agencyModel) {
        Agency agency = new Agency();
        agency.setAgencyZipCode(agencyModel.getAgencyZipCode());
        agency.setBank(Bank.fromCode(agencyModel.getBankCode()));
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
        return agency;
    }

    private void updateAgencyModel(Agency agency, AgencyModel agencyModel) {
        agencyModel.setAgencyName(agency.getAgencyName());
        agencyModel.setAgencyTelephone(agency.getAgencyTelephone());
        agencyModel.setAgencyEmail(agency.getAgencyEmail());
        agencyModel.setServices(agency.getServices());
        agencyModel.setVersion(agency.getVersion());
    }
}
