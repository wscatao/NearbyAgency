package br.com.agencies.nearbyagencies.interfaces.dto;

import java.util.List;

public record BankAgenciesResponseDto(List<BankAgenciesDto> data, CursorPaginationDto pagination) {
}
