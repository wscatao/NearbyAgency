package br.com.agencies.nearbyagencies.application.services.exception;

import br.com.agencies.nearbyagencies.domain.exception.NearbyAgencyException;
import org.springframework.http.ProblemDetail;

public class GeoCodingApiException extends NearbyAgencyException {

    private final String detail;

    public GeoCodingApiException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("GeoCodingApi Error");
        pb.setDetail(detail);
        return pb;
    }
}
