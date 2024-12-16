package br.com.santander.nearagencyapi.application.services.exception;

import br.com.santander.nearagencyapi.domain.exception.NearAgencyException;
import org.springframework.http.ProblemDetail;

public class GeoCodingApiException extends NearAgencyException {

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
