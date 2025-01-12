package br.com.agencies.nearbyagencies.infrastructure.adapters.exception;

import br.com.agencies.nearbyagencies.domain.exception.NearbyAgencyException;
import org.springframework.http.ProblemDetail;

public class OptimisticLockingException extends NearbyAgencyException {

    private final String detail;

    public OptimisticLockingException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(412);
        pb.setTitle("Version mismatch");
        pb.setDetail(detail);
        return pb;
    }
}
