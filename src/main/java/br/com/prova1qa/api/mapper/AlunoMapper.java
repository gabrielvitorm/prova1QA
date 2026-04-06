package br.com.prova1qa.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import br.com.prova1qa.api.dtos.AlunoRequest;
import br.com.prova1qa.api.dtos.AlunoResponse;
import br.com.prova1qa.api.model.Aluno;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlunoMapper {

    AlunoResponse toResponse(Aluno aluno);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nomeCompleto", source = "nomeCompleto", qualifiedByName = "trim")
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "normalizarDigitos")
    @Mapping(target = "cep", source = "cep", qualifiedByName = "normalizarDigitos")
    void preencherAluno(AlunoRequest request, @MappingTarget Aluno aluno);

    @Named("trim")
    default String trim(String value) {
        return value == null ? null : value.trim();
    }

    @Named("normalizarDigitos")
    default String normalizarDigitos(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
