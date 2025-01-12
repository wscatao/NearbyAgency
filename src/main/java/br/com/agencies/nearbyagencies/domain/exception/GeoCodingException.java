package br.com.agencies.nearbyagencies.domain.exception;

import org.springframework.http.ProblemDetail;

public class GeoCodingException extends NearbyAgencyException {

    private final String detail;

    public GeoCodingException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(422);
        pb.setTitle("GeoCoding Error");
        pb.setDetail(detail);
        return pb;
    }
}
