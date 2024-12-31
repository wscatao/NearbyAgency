package br.com.agencies.nearbyagencies.interfaces.assembler;

import br.com.agencies.nearbyagencies.domain.Agency;
import br.com.agencies.nearbyagencies.infrastructure.util.Page;
import br.com.agencies.nearbyagencies.interfaces.dto.AgencyDto;
import br.com.agencies.nearbyagencies.interfaces.dto.BankAgenciesDto;
import br.com.agencies.nearbyagencies.interfaces.dto.BankAgenciesResponseDto;
import br.com.agencies.nearbyagencies.interfaces.dto.CursorPaginationDto;

import java.util.List;
import java.util.stream.Collectors;

public class AgencyAssembler {

    private AgencyAssembler() {}

    public static BankAgenciesResponseDto toGroupedAgenciesResponseDto(Page<Agency> agencies) {

        List<BankAgenciesDto> bankAgenciesDto = agencies.getItems().stream().collect(Collectors.groupingBy(Agency::getBank))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<AgencyDto> agenciesDto = entry.getValue().stream().map(AgencyDto::fromAgency).toList();
                    return new BankAgenciesDto(entry.getKey().getName(), agenciesDto);
                }).toList();

        if(agencies.getNextPageReference() != null) {
            CursorPaginationDto cursorPaginationDto = new CursorPaginationDto(agencies.getNextPageReference(), true);
            return new BankAgenciesResponseDto(bankAgenciesDto, cursorPaginationDto);
        } else {
            return new BankAgenciesResponseDto(bankAgenciesDto, new CursorPaginationDto(null, false));
        }
    }
}
