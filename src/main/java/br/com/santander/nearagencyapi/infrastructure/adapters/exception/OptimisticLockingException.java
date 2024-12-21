package br.com.santander.nearagencyapi.infrastructure.adapters.exception;

import br.com.santander.nearagencyapi.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;

public class OptimisticLockingException extends NearAgencyException {

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
