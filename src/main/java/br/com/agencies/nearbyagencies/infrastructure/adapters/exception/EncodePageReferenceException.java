package br.com.agencies.nearbyagencies.infrastructure.adapters.exception;

import br.com.agencies.nearbyagencies.domain.exception.NearbyAgencyException;
import org.springframework.http.ProblemDetail;

public class EncodePageReferenceException extends NearbyAgencyException {

    private final String detail;

    public EncodePageReferenceException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("Error encoding page reference");
        pb.setDetail(detail);
        return pb;
    }
}
