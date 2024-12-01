package br.com.santander.nearagencyapi.interfaces.exceptions;

import br.com.santander.nearagencyapi.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllersExceptionHandlers {

    /**
     * Reminder for problem detail structure:
     * status
     * title
     * detail
     * property
     */

    @ExceptionHandler(NearAgencyException.class)
    public ProblemDetail handleNearAgencyException(NearAgencyException ex) {
        return ex.toProblemDetail();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MissingServletRequestParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle("Required parameter is missing");
        problemDetail.setDetail(ex.getBody().getDetail());
        problemDetail.setProperty("parameter-name", ex.getParameterName());
        return problemDetail;
    }

    @ExceptionHandler(MethodValidationException.class)
    public ProblemDetail handleMethodValidationException(MethodValidationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);
        problemDetail.setTitle("Invalid request parameters");
        problemDetail.setDetail("There is invalid fields on the request");
        return problemDetail;
    }
}
