package br.com.prova1qa.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErroRegraDeNegocio {

    DATA_NASCIMENTO_INVALIDA(
            "DATA_NASCIMENTO_INVALIDA",
            "dataNascimento",
            "A data de nascimento não pode ser posterior à data da matrícula."),
    IDADE_MINIMA_NAO_ATENDIDA(
            "IDADE_MINIMA_NAO_ATENDIDA",
            "dataNascimento",
            "O aluno deve ter, no mínimo, 18 anos na data da matrícula."),
    CPF_INVALIDO("CPF_INVALIDO", "cpf", "O CPF informado é inválido."),
    CEP_INVALIDO("CEP_INVALIDO", "cep", "O CEP informado é inválido."),
    SEMESTRE_INVALIDO("SEMESTRE_INVALIDO", "semestre", "O semestre informado deve ser 1 ou 2."),
    PRAZO_PRIMEIRO_SEMESTRE_ENCERRADO(
            "PRAZO_PRIMEIRO_SEMESTRE_ENCERRADO",
            "dataMatricula",
            "A matrícula do primeiro semestre deve ser realizada até março."),
    PRAZO_SEGUNDO_SEMESTRE_ENCERRADO(
            "PRAZO_SEGUNDO_SEMESTRE_ENCERRADO",
            "dataMatricula",
            "A matrícula do segundo semestre deve ser realizada até setembro.");

    private final String codigo;
    private final String campo;
    private final String mensagem;
}
