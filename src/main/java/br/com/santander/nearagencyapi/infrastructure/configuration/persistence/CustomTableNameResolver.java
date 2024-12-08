package br.com.santander.nearagencyapi.infrastructure.configuration.persistence;

import br.com.santander.nearagencyapi.infrastructure.model.AgencyModel;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomTableNameResolver implements DynamoDbTableNameResolver {

    @Override
    public <T> String resolve(Class<T> domainClass) {

        // Retorne o nome da tabela desejado
        if (domainClass.equals(AgencyModel.class)) {
            return "AgencyRegistration";
        }
        // Adicione outras resoluções de nome de tabela conforme necessário
        return domainClass.getSimpleName().toLowerCase();
    }
}
