package br.com.santander.nearagencyapi.interfaces;

import br.com.santander.nearagencyapi.application.usecases.AgencyUseCases;
import br.com.santander.nearagencyapi.domain.Agency;
import br.com.santander.nearagencyapi.interfaces.dto.AgencyDto;
import com.google.maps.errors.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/agencies")
public class AgencyController {

    private final AgencyUseCases agencyUseCases;

    public AgencyController(AgencyUseCases agencyUseCases) {
        this.agencyUseCases = agencyUseCases;
    }

    @GetMapping("/agencies")
    public ResponseEntity<List<AgencyDto>> getAgencies() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/agencies/{id}")
    public ResponseEntity<Void> getAgencyById() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/agencies/cep/{cep}")
    public ResponseEntity<Void> getAgencyByCep() {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> postAgency(@RequestBody AgencyDto agencyDto) throws IOException, InterruptedException, ApiException {
        var agency = Agency.toAgency(agencyDto);
        agencyUseCases.createAgency(agency);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/agencies/{id}")
    public ResponseEntity<Void> putAgency() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/agencies/{id}")
    public ResponseEntity<Void> deleteAgency() {
        return ResponseEntity.ok().build();
    }
}
