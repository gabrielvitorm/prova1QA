package br.com.prova1qa.api.service;

import java.time.Period;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.prova1qa.api.dtos.AlunoRequest;
import br.com.prova1qa.api.dtos.AlunoResponse;
import br.com.prova1qa.api.exception.EntidadeNaoEncontradaException;
import br.com.prova1qa.api.exception.ErroRegraDeNegocio;
import br.com.prova1qa.api.exception.ViolacaoRegraDeNegocioException;
import br.com.prova1qa.api.mapper.AlunoMapper;
import br.com.prova1qa.api.model.Aluno;
import br.com.prova1qa.api.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository alunoRepository;
    private final AlunoMapper alunoMapper;

    @Override
    @Transactional
    public AlunoResponse cadastrar(AlunoRequest request) {
        validarRegrasDeNegocio(request);

        Aluno aluno = new Aluno();
        alunoMapper.preencherAluno(request, aluno);

        return alunoMapper.toResponse(alunoRepository.save(aluno));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlunoResponse> listar() {
        return alunoRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCompleto"))
                .stream()
                .map(alunoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlunoResponse buscarPorId(Long id) {
        return alunoMapper.toResponse(buscarEntidade(id));
    }

    @Override
    @Transactional
    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno aluno = buscarEntidade(id);
        validarRegrasDeNegocio(request);
        alunoMapper.preencherAluno(request, aluno);

        return alunoMapper.toResponse(alunoRepository.save(aluno));
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscarEntidade(id);
        alunoRepository.delete(aluno);
    }

    private Aluno buscarEntidade(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno com ID " + id + " não encontrado."));
    }

    private void validarRegrasDeNegocio(AlunoRequest request) {
        String cpf = normalizarDigitos(request.cpf());
        String cep = normalizarDigitos(request.cep());

        validarMaioridade(request);
        validarCpf(cpf);
        validarCep(cep);
        validarSemestre(request.semestre());
        validarPrazoMatricula(request);
    }

    private void validarMaioridade(AlunoRequest request) {
        if (request.dataNascimento().isAfter(request.dataMatricula())) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.DATA_NASCIMENTO_INVALIDA);
        }

        int idade = Period.between(request.dataNascimento(), request.dataMatricula()).getYears();
        if (idade < 18) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.IDADE_MINIMA_NAO_ATENDIDA);
        }
    }

    private void validarCpf(String cpf) {
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1 || !cpfValido(cpf)) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.CPF_INVALIDO);
        }
    }

    private void validarCep(String cep) {
        if (cep.length() != 8) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.CEP_INVALIDO);
        }
    }

    private void validarSemestre(Integer semestre) {
        if (semestre == null || (semestre != 1 && semestre != 2)) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.SEMESTRE_INVALIDO);
        }
    }

    private void validarPrazoMatricula(AlunoRequest request) {
        int mesAtual = request.dataMatricula().getMonthValue();
        Integer semestre = request.semestre();

        if (semestre == 1 && mesAtual > 3) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.PRAZO_PRIMEIRO_SEMESTRE_ENCERRADO);
        }

        if (semestre == 2 && mesAtual > 9) {
            throw new ViolacaoRegraDeNegocioException(ErroRegraDeNegocio.PRAZO_SEGUNDO_SEMESTRE_ENCERRADO);
        }
    }

    private String normalizarDigitos(String valor) {
        return valor.replaceAll("\\D", "");
    }

    private boolean cpfValido(String cpf) {
        return calcularDigito(cpf, 10) == Character.getNumericValue(cpf.charAt(9))
                && calcularDigito(cpf, 11) == Character.getNumericValue(cpf.charAt(10));
    }

    private int calcularDigito(String cpf, int pesoInicial) {
        int soma = 0;
        int limite = pesoInicial - 1;

        for (int i = 0; i < limite; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (pesoInicial - i);
        }

        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }
}
