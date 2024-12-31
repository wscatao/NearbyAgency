package br.com.agencies.nearbyagencies.interfaces.dto;

import java.util.List;

public record BankAgenciesDto(String bank, List<AgencyDto> agencies) {
}
