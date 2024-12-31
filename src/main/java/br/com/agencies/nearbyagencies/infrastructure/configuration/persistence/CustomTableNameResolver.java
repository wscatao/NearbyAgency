package br.com.agencies.nearbyagencies.infrastructure.configuration.persistence;

import br.com.agencies.nearbyagencies.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomTableNameResolver implements DynamoDbTableNameResolver {

    @Override
    public <T> String resolve(Class<T> domainClass) {

        // Retorne o nome da tabela desejado
        if (domainClass.equals(AgencyModel.class)) {
            return "agency_registration";
        }
        // Adicione outras resoluções de nome de tabela conforme necessário
        return domainClass.getSimpleName().toLowerCase();
    }
}
