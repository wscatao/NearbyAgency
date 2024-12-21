package br.com.santander.nearagencyapi.interfaces.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateAgencyDto {

    @NotBlank(message = "agency name is required")
    private String agencyName;

    @Size(max = 15, message = "The agency telephone must have a maximum of 15 characters")
    @NotBlank(message = "The agency telephone is required")
    private String agencyTelephone;

    @Email(message = "Invalid email")
    @NotBlank(message = "The agency email is required")
    private String agencyEmail;

    @NotNull(message = "The services list cannot be null")
    private List<String> services;
}
