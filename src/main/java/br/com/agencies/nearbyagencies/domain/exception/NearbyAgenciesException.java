package br.com.agencies.nearbyagencies.domain.exception;

import org.springframework.http.ProblemDetail;

public class NearbyAgenciesException extends NearbyAgencyException {

    private final String detail;

    public NearbyAgenciesException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(422);
        pb.setTitle("Nearby Agencies Error");
        pb.setDetail(detail);
        return pb;
    }
}
