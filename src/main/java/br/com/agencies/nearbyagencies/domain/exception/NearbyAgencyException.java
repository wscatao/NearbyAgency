package br.com.agencies.nearbyagencies.domain.exception;

import org.springframework.http.ProblemDetail;

public abstract class NearbyAgencyException extends RuntimeException {

    public NearbyAgencyException(String message) {
        super(message);
    }

    public NearbyAgencyException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("NearAngency Internal Server Error");
        pb.setDetail("Contact the support team");
        return pb;
    }
}
