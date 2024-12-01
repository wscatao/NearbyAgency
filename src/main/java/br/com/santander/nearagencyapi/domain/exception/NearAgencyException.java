package br.com.santander.nearagencyapi.domain.exception;

import org.springframework.http.ProblemDetail;

public abstract class NearAgencyException extends RuntimeException {

    public NearAgencyException(String message) {
        super(message);
    }

    public NearAgencyException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("NearAngency Internal Server Error");
        pb.setDetail("Contact the support team");
        return pb;
    }
}
