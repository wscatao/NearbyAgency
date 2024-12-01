package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.domain.Address;
import br.com.santander.nearagencyapi.domain.gateway.ZipCodeClientGateway;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
