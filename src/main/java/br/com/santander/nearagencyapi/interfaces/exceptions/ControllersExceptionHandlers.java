package br.com.santander.nearagencyapi.interfaces.exceptions;

import br.com.santander.nearagencyapi.domain.exception.NearAgencyException;
import br.com.santander.nearagencyapi.interfaces.exceptions.dto.InvalidParamDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        var invalidParams = constraintViolations.stream().map(constraintViolation ->
                new InvalidParamDto(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessageTemplate(),
                        constraintViolation.getInvalidValue().toString()));


        problemDetail.setTitle("Validation error");
        problemDetail.setDetail("One or more parameters are invalid");
        problemDetail.setProperty("invalid-parameters", invalidParams);
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(400);

        var invalidParams = ex.getBindingResult().getFieldErrors().stream().map(fieldError ->
                new InvalidParamDto(fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : "null"));

        problemDetail.setTitle("Validation error");
        problemDetail.setDetail("One or more parameters are invalid");
        problemDetail.setProperty("invalid-parameters", invalidParams);
        return problemDetail;
    }
}
