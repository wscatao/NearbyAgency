package br.com.agencies.nearbyagencies.domain.constant;

import lombok.Getter;

@Getter
public enum Bank {
    ITAU("341", "Itaú Unibanco S.A."),
    BRADESCO("237", "Banco Bradesco S.A."),
    SANTANDER("033", "Banco Santander (Brasil) S.A."),
    CAIXA("104", "Caixa Econômica Federal"),
    BANCO_DO_BRASIL("001", "Banco do Brasil S.A."),
    NUBANK("260", "Nu Pagamentos S.A."),
    INTER("077", "Banco Inter S.A."),
    ORIGINAL("212", "Banco Original S.A."),
    C6("336", "Banco C6 S.A."),
    BMG("318", "Banco BMG S.A."),
    PAN("623", "Banco Pan S.A."),
    SAFRA("422", "Banco Safra S.A."),
    BANCO_24H("000", "Banco 24Horas S.A."),;

    private final String code;
    private final String name;

    Bank(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Bank fromCode(String code) {
        for (Bank bank : Bank.values()) {
            if (bank.getCode().equals(code)) {
                return bank;
            }
        }
        throw new IllegalArgumentException("Invalid bank code: " + code);
    }
}
