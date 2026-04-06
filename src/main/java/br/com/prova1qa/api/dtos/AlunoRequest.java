package br.com.prova1qa.api.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record AlunoRequest(
        @NotBlank(message = "Nome completo e obrigatorio")
        String nomeCompleto,

        @NotBlank(message = "CPF e obrigatorio")
        String cpf,

        @NotBlank(message = "CEP e obrigatorio")
        String cep,

        @NotNull(message = "Data de nascimento e obrigatoria")
        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        @NotNull(message = "Data da matricula e obrigatoria")
        LocalDate dataMatricula,

        @NotNull(message = "Semestre e obrigatorio")
        Integer semestre) {
}
