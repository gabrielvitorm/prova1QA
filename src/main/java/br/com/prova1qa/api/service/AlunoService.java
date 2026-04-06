package br.com.prova1qa.api.service;

import java.util.List;

import br.com.prova1qa.api.dtos.AlunoRequest;
import br.com.prova1qa.api.dtos.AlunoResponse;

public interface AlunoService {

    AlunoResponse cadastrar(AlunoRequest request);

    List<AlunoResponse> listar();

    AlunoResponse buscarPorId(Long id);

    AlunoResponse atualizar(Long id, AlunoRequest request);

    void excluir(Long id);
}
