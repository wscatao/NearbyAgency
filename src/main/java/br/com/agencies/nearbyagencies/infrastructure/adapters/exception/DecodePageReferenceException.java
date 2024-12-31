package br.com.agencies.nearbyagencies.infrastructure.adapters.exception;

import br.com.agencies.nearbyagencies.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;

public class DecodePageReferenceException extends NearAgencyException {

    private final String detail;

    public DecodePageReferenceException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("Error decoding page reference");
        pb.setDetail(detail);
        return pb;
    }
}
