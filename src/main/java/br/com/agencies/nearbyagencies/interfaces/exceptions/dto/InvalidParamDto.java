package br.com.agencies.nearbyagencies.interfaces.exceptions.dto;

public record InvalidParamDto(String parameterName, String message, String value) {}
