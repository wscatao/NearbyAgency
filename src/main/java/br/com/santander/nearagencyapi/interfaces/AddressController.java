package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.domain.Address;
import br.com.santander.nearagencyapi.domain.gateway.ZipCodeClientGateway;
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
    public Address getAddress(@RequestParam(value = "zipcode") String zipcode) {
        return zipCodeClientGateway.getAddress(zipcode);
    }
}
