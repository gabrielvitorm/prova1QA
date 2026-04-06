package br.com.prova1qa.api.exception;

import lombok.Getter;

@Getter
public class ViolacaoRegraDeNegocioException extends RuntimeException {

    private final String codigo;
    private final String campo;

    public ViolacaoRegraDeNegocioException(ErroRegraDeNegocio erroRegraDeNegocio) {
        super(erroRegraDeNegocio.getMensagem());
        this.codigo = erroRegraDeNegocio.getCodigo();
        this.campo = erroRegraDeNegocio.getCampo();
    }
}
