package br.com.prova1qa.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegraNegocioErro {

    IDADE_INVALIDA("IDADE_INVALIDA", "dataNascimento", "Data de nascimento nao pode ser depois da data da matricula"),
    ALUNO_MENOR_DE_IDADE("ALUNO_MENOR_DE_IDADE", "dataNascimento", "Aluno precisa ter pelo menos 18 anos"),
    CPF_INVALIDO("CPF_INVALIDO", "cpf", "CPF invalido"),
    CEP_INVALIDO("CEP_INVALIDO", "cep", "CEP invalido"),
    SEMESTRE_INVALIDO("SEMESTRE_INVALIDO", "semestre", "Semestre deve ser 1 ou 2"),
    PRAZO_MATRICULA_1_SEMESTRE_ENCERRADO(
            "PRAZO_MATRICULA_1_SEMESTRE_ENCERRADO",
            "dataMatricula",
            "Matricula do primeiro semestre so pode ser feita ate marco"),
    PRAZO_MATRICULA_2_SEMESTRE_ENCERRADO(
            "PRAZO_MATRICULA_2_SEMESTRE_ENCERRADO",
            "dataMatricula",
            "Matricula do segundo semestre so pode ser feita ate setembro");

    private final String codigo;
    private final String campo;
    private final String mensagem;
}
