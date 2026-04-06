package br.com.prova1qa.api.dtos;

import java.time.LocalDate;

public record AlunoResponse(
        Long id,
        String nomeCompleto,
        String cpf,
        String cep,
        LocalDate dataNascimento,
        LocalDate dataMatricula,
        Integer semestre) {
}
