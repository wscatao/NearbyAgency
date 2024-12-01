package br.com.santander.nearagencyapi.domain.exception;

import org.springframework.http.ProblemDetail;

public class ViaCepGetAddressException extends NearAgencyException {

    private final String detail;

    public ViaCepGetAddressException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(422);
        pb.setTitle("ViaCep Get Address Error");
        pb.setDetail(detail);
        return pb;
    }
}
