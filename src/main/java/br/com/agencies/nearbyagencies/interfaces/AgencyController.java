package br.com.agencies.nearbyagencies.interfaces;

import br.com.agencies.nearbyagencies.application.usecases.AgencyUseCases;
import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import br.com.agencies.nearbyagencies.interfaces.assembler.AgencyAssembler;
import br.com.agencies.nearbyagencies.interfaces.dto.AgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.BankAgenciesResponseDto;
import br.com.agencies.nearbyagencies.interfaces.dto.CreateAgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.UpdateAgencyDto;
import com.google.maps.errors.ApiException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("{bank-code}")
    public ResponseEntity<BankAgenciesResponseDto> getNearbyAgencies(
            @PathVariable(value = "bank-code")
            @NotBlank(message = "bank code is required")
            @Pattern(regexp = "^\\d{3}$", message = "bank code must have 3 digits")
            String bankCode,
            @RequestParam(value = "zip-code")
            @NotBlank(message = "zip code is required")
            @Pattern(regexp = "^\\d{8}$", message = "zip code must have 8 digits")
            String zipCode,
            @RequestParam(value = "radius", required = false, defaultValue = "5")
            @Min(value = 4, message = "min radius is 4")
            @Max(value = 5, message = "max radius is 5")
            Integer radius,
            @RequestParam(value = "next-page-token", required = false)
            String nextPageToken,
            @RequestParam(value = "limit", required = false, defaultValue = "10")
            @Min(value = 1, message = "min limit is 1")
            int limit
    ) {
        Page<Agency> nearbyAgencies = agencyUseCases.getNearbyAgencies(bankCode, zipCode, radius, nextPageToken, limit);
        BankAgenciesResponseDto agencies = AgencyAssembler.toGroupedAgenciesResponseDto(nearbyAgencies);
        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "application/json")
                .body(agencies);
    }

    @GetMapping("/{bank-code}/{agency-number}")
    public ResponseEntity<AgencyDto> getAgencyByAgencyCode(
            @PathVariable(value = "bank-code")
            @NotBlank(message = "bank code is required")
            @Pattern(regexp = "^\\d{3}$", message = "bank code must have 3 digits")
            String bankCode,
            @PathVariable(value = "agency-number")
            @NotBlank(message = "agency number is required")
            @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
            String agencyNumber) {

        Agency agency = agencyUseCases.getAgency(bankCode, agencyNumber);
        AgencyDto createAgencyDto = AgencyDto.fromAgency(agency);

        return ResponseEntity.ok()
                .header("Cache-Control", "no-cache")
                .header("Content-Type", "application/json")
                .eTag(agency.getVersion().toString())
                .body(createAgencyDto);
    }

    @PostMapping
    public ResponseEntity<Void> createAgency(@RequestBody @Validated CreateAgencyDto createAgencyDto) throws IOException, InterruptedException, ApiException {
        var agency = Agency.toAgency(createAgencyDto);

        agencyUseCases.createAgency(agency);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{bank-code}/{agency-number}")
                .buildAndExpand(agency.getBank().getCode(), agency.getAgencyNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{bank-code}/{agency-number}")
    public ResponseEntity<Void> updateAgency(
            @RequestBody
            @Validated
            UpdateAgencyDto updateAgencyDto,
            @PathVariable(value = "bank-code")
            @NotBlank(message = "bank code is required")
            @Pattern(regexp = "^\\d{3}$", message = "bank code must have 3 digits")
            String bankCode,
            @PathVariable(value = "agency-number")
            @NotBlank(message = "agency number is required")
            @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
            String agencyNumber,
            @RequestHeader("If-Match")
            @NotBlank(message = "If-Match header is required")
            String ifMatch) {

        var agency = Agency.toAgency(updateAgencyDto, bankCode, agencyNumber, ifMatch);
        agencyUseCases.updateAgency(agency);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bank-code}/{agency-number}")
    public ResponseEntity<Void> deleteAgency(
            @PathVariable(value = "bank-code")
            @NotBlank(message = "bank code is required")
            @Pattern(regexp = "^\\d{3}$", message = "bank code must have 3 digits")
            String bankCode,
            @PathVariable(value = "agency-number")
            @NotBlank(message = "agency number is required")
            @Pattern(regexp = "^\\d{4}$", message = "agency number must have 4 digits")
            String agencyNumber) {

        Agency agency = Agency.toAgency(bankCode, agencyNumber);
        agencyUseCases.deleteAgency(agency);
        return ResponseEntity.noContent().build();
    }
}
