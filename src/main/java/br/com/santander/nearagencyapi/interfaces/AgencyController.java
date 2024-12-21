package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.application.usecases.AgencyUseCases;
import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
import br.com.santander.nearagencyapi.interfaces.dto.UpdateAgencyDto;
import com.google.maps.errors.ApiException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/agencies")
@Validated
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
            @PathVariable(value = "zip-code")
            @NotBlank(message = "zip code is required")
            @Pattern(regexp = "^\\d{8}$", message = "zip code must have 8 digits")
            String zipcode,
            @PathVariable(value = "agency-number")
            @NotBlank(message = "agency number is required")
            @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
            String agencyNumber) {

        Agency agency = agencyUseCases.getAgencyByCepAndNumber(zipcode, agencyNumber);
        AgencyDto agencyDto = AgencyDto.fromAgency(agency);

        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "application/json")
                .eTag(agency.getVersion().toString())
                .body(agencyDto);
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

    @PatchMapping("/{zip-code}/{agency-number}")
    public ResponseEntity<Void> updateAgency(
            @RequestBody @Validated UpdateAgencyDto updateAgencyDto,
            @PathVariable(value = "zip-code")
            @NotBlank(message = "zip code is required")
            @Pattern(regexp = "^\\d{8}$", message = "zip code must have 8 digits")
            String zipCode,
            @PathVariable(value = "agency-number")
            @NotBlank(message = "agency number is required")
            @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
            String agencyNumber,
            @RequestHeader("If-Match")
            @NotBlank(message = "If-Match header is required")
            String ifMatch) {

        var agency = Agency.toAgency(updateAgencyDto, zipCode, agencyNumber, ifMatch);
        agencyUseCases.updateAgency(agency);

        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/agencies/{id}")
//    public ResponseEntity<Void> deleteAgency() {
//        return ResponseEntity.ok().build();
//    }
}
