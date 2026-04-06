package br.com.prova1qa.api.exception;

import lombok.Getter;

@Getter
public class RegraDeNegocioException extends RuntimeException {

    private final String codigo;
    private final String campo;

    public RegraDeNegocioException(RegraNegocioErro regraNegocioErro) {
        super(regraNegocioErro.getMensagem());
        this.codigo = regraNegocioErro.getCodigo();
        this.campo = regraNegocioErro.getCampo();
    }
}
