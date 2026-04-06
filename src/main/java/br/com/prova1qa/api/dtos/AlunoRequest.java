package br.com.prova1qa.api.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record AlunoRequest(
        @NotBlank(message = "Nome completo é obrigatório.")
        String nomeCompleto,

        @NotBlank(message = "CPF é obrigatório.")
        String cpf,

        @NotBlank(message = "CEP é obrigatório.")
        String cep,

        @NotNull(message = "Data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve estar no passado.")
        LocalDate dataNascimento,

        @NotNull(message = "Data de matrícula é obrigatória.")
        LocalDate dataMatricula,

        @NotNull(message = "Semestre é obrigatório.")
        Integer semestre) {
}
