package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.application.usecases.AgencyUseCases;
import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
import com.google.maps.errors.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/agencies")
public class AgencyController {

    private final AgencyUseCases agencyUseCases;

    public AgencyController(AgencyUseCases agencyUseCases) {
        this.agencyUseCases = agencyUseCases;
    }

//    @GetMapping
//    public ResponseEntity<List<AgencyDto>> getAgencies() {
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/{zip-code}/{agency-number}")
    public ResponseEntity<AgencyDto> getAgencyByCepAndNumber(
            @PathVariable(value = "zip-code") String zipcode,
            @PathVariable(value = "agency-number") String agencyNumber) {
        Agency agency = agencyUseCases.getAgencyByCepAndNumber(zipcode, agencyNumber);
        AgencyDto agencyDto = AgencyDto.fromAgency(agency);
        return ResponseEntity.ok(agencyDto);
    }

//    @GetMapping("/agencies/cep/{cep}")
//    public ResponseEntity<Void> getAgencyByCep() {
//        return ResponseEntity.ok().build();
//    }

    @PostMapping
    public ResponseEntity<Void> createAgency(@RequestBody @Validated AgencyDto agencyDto) throws IOException, InterruptedException, ApiException {
        var agency = Agency.toAgency(agencyDto);

        agencyUseCases.createAgency(agency);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{zip-code}/{agency-number}")
                .buildAndExpand(agency.getAgencyZipCode(), agency.getAgencyNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

//    @PutMapping("/agencies/{id}")
//    public ResponseEntity<Void> putAgency() {
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/agencies/{id}")
//    public ResponseEntity<Void> deleteAgency() {
//        return ResponseEntity.ok().build();
//    }
}
