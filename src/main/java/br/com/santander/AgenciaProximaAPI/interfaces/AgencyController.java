package br.com.santander.AgenciaProximaAPI.interfaces;

import br.com.santander.AgenciaProximaAPI.interfaces.dto.AgencyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agencies")
public class AgencyController {

    @GetMapping("/agencies")
    public ResponseEntity<List<AgencyDTO>> getAgencies() {
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

    @PostMapping("/agencies")
    public ResponseEntity<Void> postAgency() {
        return ResponseEntity.ok().build();
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
