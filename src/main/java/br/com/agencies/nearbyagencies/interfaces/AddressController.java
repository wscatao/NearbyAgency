package br.com.agencies.nearbyagencies.interfaces;

import br.com.agencies.nearbyagencies.domain.Address;
import br.com.agencies.nearbyagencies.domain.gateway.ZipCodeClientGateway;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    public AddressController(ZipCodeClientGateway zipCodeClientGateway) {
        this.zipCodeClientGateway = zipCodeClientGateway;
    }

    @GetMapping
    public Address getAddress(
            @RequestParam(value = "zipcode")
            @NotNull(message = "zipcode is required")
            @Size(min = 8, max = 8, message = "zipcode must have 8 characters")
            String zipcode) {
        return zipCodeClientGateway.getAddress(zipcode);
    }
}
