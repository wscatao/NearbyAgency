package br.com.agencies.nearbyagencies.infrastructure.adapters.restclient;

import br.com.agencies.nearbyagencies.domain.Address;
import br.com.agencies.nearbyagencies.domain.exception.ViaCepGetAddressException;
import br.com.agencies.nearbyagencies.domain.gateway.ZipCodeClientGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ZipCodeClientImpl implements ZipCodeClientGateway {

    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(ZipCodeClientImpl.class);

    public ZipCodeClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public Address getAddress(String zipcode) {

        logger.info("Requesting address for zipcode: {}", zipcode);

        try {
            Address address = restClient.get()
                    .uri("ws/{zipcode}/json/", zipcode)
                    .retrieve()
                    .body(Address.class);

            logger.info("Address found: {}", address);

            return address;
        } catch (Exception e) {
            String errorDetail = "Error fetching address for zipcode: " + zipcode;
            logger.error(errorDetail);
            throw new ViaCepGetAddressException(errorDetail);
        }
    }
}
