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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AgencyPersistenceAdapterImpl.class);
    private final DynamoDbTemplate dynamoDbTemplate;

    public AgencyPersistenceAdapterImpl(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    @Override
    public void save(Agency agency) {
        logger.info("Saving agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        AgencyModel agencyModel = toAgencyModel(agency);
        dynamoDbTemplate.save(agencyModel);

        logger.info("Agency saved with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
    }

    @Override
    public Optional<Agency> findByBankCodeAndAgencyNumber(String bankCode, String agencyNumber) {
        logger.info("Finding agency with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);

        Key key = Key.builder()
                .partitionValue(bankCode)
                .sortValue(agencyNumber)
                .build();

        Optional<Agency> agency = Optional.ofNullable(dynamoDbTemplate.load(key, AgencyModel.class))
                .map(this::toAgency);

        if (agency.isPresent()) {
            logger.info("Agency found with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);
        } else {
            logger.warn("Agency not found with bankCode: {}, agencyNumber: {}", bankCode, agencyNumber);
        }

        return agency;
    }

    @Override
    public void update(Agency agency) {
        logger.info("Updating agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        Key key = Key.builder()
                .partitionValue(agency.getBank().getCode())
                .sortValue(agency.getAgencyNumber())
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if (agencyModel != null) {
            updateAgencyModel(agency, agencyModel);
            try {
                dynamoDbTemplate.update(agencyModel);

                logger.info("Agency updated with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
            } catch (ConditionalCheckFailedException e) {
                logger.error("Version mismatch - update failed for agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber(), e);
                throw new OptimisticLockingException("Version mismatch - update failed");
            }
        } else {
            logger.warn("Agency not found for update with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
        }
    }

    @Override
    public void delete(Agency agency) {
        logger.info("Deleting agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());

        Key key = Key.builder()
                .partitionValue(agency.getBank().getCode())
                .sortValue(agency.getAgencyNumber())
                .build();

        AgencyModel agencyModel = dynamoDbTemplate.load(key, AgencyModel.class);

        if (agencyModel != null) {
            try {
                dynamoDbTemplate.delete(agencyModel);

                logger.info("Agency deleted with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
            } catch (ConditionalCheckFailedException e) {
                logger.error("Version mismatch - delete failed for agency with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber(), e);
                throw new OptimisticLockingException("Version mismatch - delete failed");
            }
        } else {
            logger.warn("Agency not found for delete with bankCode: {}, agencyNumber: {}", agency.getBank().getCode(), agency.getAgencyNumber());
        }
    }

    @Override
    public Page<Agency> findByBankAndGeoHash(String bankCode, String geoHashPrefix, String nextPageToken, int limit) {
        logger.info("Finding nearby agencies with bankCode: {}, geoHashPrefix: {}, nextPageToken: {}, limit: {}", bankCode, geoHashPrefix, nextPageToken, limit);

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

        Page<Agency> page = dynamoDbTemplate.query(request, AgencyModel.class)
                .stream().findFirst().map(p -> {
                    Map<String, AttributeValue> stringAttributeValueMap = p.lastEvaluatedKey();
                    List<AgencyModel> items = p.items().stream().limit(limit).toList();
                    return createPaginatedList(items, stringAttributeValueMap, limit);
                }).orElse(new Page<>(List.of(), null));

        logger.info("Found {} nearby agencies", page.getItems().size());

        return page;
    }

    private Page<Agency> createPaginatedList(List<AgencyModel> items, Map<String, AttributeValue> stringAttributeValueMap, int limit) {
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

            List<Agency> agencies = items.stream().limit(limit).map(this::toAgency).toList();

            return new Page<>(agencies, nextPageReference);

        } catch (Exception e) {
            logger.error("Error creating paginated list", e);
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
