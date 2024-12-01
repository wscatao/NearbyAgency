package br.com.santander.nearagencyapi.interfaces.exceptions;

import br.com.santander.nearagencyapi.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersExceptionHandlers {

    @ExceptionHandler(NearAgencyException.class)
    public ProblemDetail handleNearAgencyException(NearAgencyException ex) {
        return ex.toProblemDetail();
    }
}
