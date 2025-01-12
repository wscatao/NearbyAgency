package br.com.agencies.nearbyagencies.interfaces;

import br.com.agencies.nearbyagencies.domain.Address;
import br.com.agencies.nearbyagencies.domain.gateway.ZipCodeClientGateway;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/address")
public class AddressController {

    private final ZipCodeClientGateway zipCodeClientGateway;
    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    public AddressController(ZipCodeClientGateway zipCodeClientGateway) {
        this.zipCodeClientGateway = zipCodeClientGateway;
    }

    @GetMapping
    public Address getAddress(
            @RequestParam(value = "zipcode")
            @NotNull(message = "zipcode is required")
            @Size(min = 8, max = 8, message = "zipcode must have 8 characters")
            String zipcode) {

        logger.info("Requesting address for zipcode: {}", zipcode);

        Address address = zipCodeClientGateway.getAddress(zipcode);

        logger.info("Address found: {}", address);

        return address;
    }
}
