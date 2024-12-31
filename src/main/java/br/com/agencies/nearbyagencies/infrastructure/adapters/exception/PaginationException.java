package br.com.agencies.nearbyagencies.infrastructure.adapters.exception;

import br.com.agencies.nearbyagencies.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;

public class PaginationException extends NearAgencyException {

    private final String detail;

    public PaginationException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(500);
        pb.setTitle("Pagination error");
        pb.setDetail(detail);
        return pb;
    }
}
