package br.com.santander.nearagencyapi.interfaces.exceptions.dto;

public record InvalidParamDto(String parameterName, String message, String value) {}
