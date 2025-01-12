package br.com.agencies.nearbyagencies.domain.exception;

import org.springframework.http.ProblemDetail;

public class AgencyNotFoundException extends NearbyAgencyException {

    private final String detail;

    public AgencyNotFoundException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(404);
        pb.setTitle("Agency not found");
        pb.setDetail(detail);
        return pb;
    }
}
