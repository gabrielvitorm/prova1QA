package br.com.prova1qa.api.aluno;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Sort;

import br.com.prova1qa.api.dtos.AlunoRequest;
import br.com.prova1qa.api.dtos.AlunoResponse;
import br.com.prova1qa.api.exception.EntidadeNaoEncontradaException;
import br.com.prova1qa.api.exception.ViolacaoRegraDeNegocioException;
import br.com.prova1qa.api.mapper.AlunoMapper;
import br.com.prova1qa.api.model.Aluno;
import br.com.prova1qa.api.repository.AlunoRepository;
import br.com.prova1qa.api.service.AlunoServiceImpl;

class AlunoServiceImplTest {

    private AlunoRepository alunoRepository;
    private AlunoMapper alunoMapper;
    private AlunoServiceImpl alunoService;

    @BeforeEach
    void setUp() {
        alunoRepository = mock(AlunoRepository.class);
        alunoMapper = mock(AlunoMapper.class);
        alunoService = new AlunoServiceImpl(alunoRepository, alunoMapper);
    }

    @Test
    void deveCadastrarAlunoQuandoDadosForemValidos() {
        AlunoRequest request = criarRequestValidoParaPrimeiroSemestre();
        AlunoResponse responseEsperado = criarResponse(
                1L,
                "Gabriel Vitor Martins",
                "52998224725",
                "01001000",
                LocalDate.of(2000, 5, 10),
                LocalDate.of(2026, 2, 10),
                1);

        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno aluno = invocation.getArgument(0);
            aluno.setId(1L);
            return aluno;
        });
        when(alunoMapper.toResponse(any(Aluno.class))).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.cadastrar(request);

        ArgumentCaptor<Aluno> alunoCaptor = ArgumentCaptor.forClass(Aluno.class);

        verify(alunoMapper).preencherAluno(eq(request), alunoCaptor.capture());
        verify(alunoRepository).save(same(alunoCaptor.getValue()));
        verify(alunoMapper).toResponse(same(alunoCaptor.getValue()));
        verifyNoMoreInteractions(alunoRepository, alunoMapper);

        assertEquals(responseEsperado, response);
    }

    @Test
    void devePermitirCadastroQuandoAlunoTiverExatamenteDezoitoAnos() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Limite Dezoito",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(2008, 3, 10),
                LocalDate.of(2026, 3, 10),
                1);
        AlunoResponse responseEsperado = criarResponse(
                2L,
                "Aluno Limite Dezoito",
                "52998224725",
                "01001000",
                LocalDate.of(2008, 3, 10),
                LocalDate.of(2026, 3, 10),
                1);

        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno aluno = invocation.getArgument(0);
            aluno.setId(2L);
            return aluno;
        });
        when(alunoMapper.toResponse(any(Aluno.class))).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.cadastrar(request);

        verify(alunoRepository).save(any(Aluno.class));
        verify(alunoMapper).preencherAluno(eq(request), any(Aluno.class));
        verify(alunoMapper).toResponse(any(Aluno.class));
        assertEquals(responseEsperado, response);
    }

    @Test
    void devePermitirPrimeiroSemestreQuandoMatriculaForEmMarco() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Marco",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 3, 31),
                1);
        AlunoResponse responseEsperado = criarResponse(
                3L,
                "Aluno Marco",
                "52998224725",
                "01001000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 3, 31),
                1);

        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno aluno = invocation.getArgument(0);
            aluno.setId(3L);
            return aluno;
        });
        when(alunoMapper.toResponse(any(Aluno.class))).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.cadastrar(request);

        verify(alunoRepository).save(any(Aluno.class));
        assertEquals(responseEsperado, response);
    }

    @Test
    void devePermitirSegundoSemestreQuandoMatriculaForEmSetembro() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Setembro",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 9, 30),
                2);
        AlunoResponse responseEsperado = criarResponse(
                4L,
                "Aluno Setembro",
                "52998224725",
                "01001000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 9, 30),
                2);

        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno aluno = invocation.getArgument(0);
            aluno.setId(4L);
            return aluno;
        });
        when(alunoMapper.toResponse(any(Aluno.class))).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.cadastrar(request);

        verify(alunoRepository).save(any(Aluno.class));
        assertEquals(responseEsperado, response);
    }

    @Test
    void deveListarAlunosOrdenadosPorNomeCompleto() {
        Aluno alunoA = criarAluno(1L);
        Aluno alunoB = criarAluno(2L);
        AlunoResponse responseA = criarResponse(
                1L,
                "Ana Clara",
                "52998224725",
                "01001000",
                LocalDate.of(1999, 3, 1),
                LocalDate.of(2026, 2, 15),
                1);
        AlunoResponse responseB = criarResponse(
                2L,
                "Bruno Lima",
                "11144477735",
                "01310100",
                LocalDate.of(1998, 8, 20),
                LocalDate.of(2026, 9, 10),
                2);

        when(alunoRepository.findAll(any(Sort.class))).thenReturn(List.of(alunoA, alunoB));
        when(alunoMapper.toResponse(alunoA)).thenReturn(responseA);
        when(alunoMapper.toResponse(alunoB)).thenReturn(responseB);

        List<AlunoResponse> response = alunoService.listar();

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(alunoRepository).findAll(sortCaptor.capture());
        verify(alunoMapper).toResponse(alunoA);
        verify(alunoMapper).toResponse(alunoB);
        verifyNoMoreInteractions(alunoRepository, alunoMapper);

        Sort.Order order = sortCaptor.getValue().getOrderFor("nomeCompleto");

        assertAll(
                () -> assertEquals(Sort.Direction.ASC, order.getDirection()),
                () -> assertIterableEquals(List.of(responseA, responseB), response));
    }

    @Test
    void deveBuscarAlunoPorIdQuandoExistir() {
        Aluno aluno = criarAluno(1L);
        AlunoResponse responseEsperado = criarResponse(
                1L,
                "Gabriel Vitor Martins",
                "52998224725",
                "01001000",
                LocalDate.of(2000, 5, 10),
                LocalDate.of(2026, 2, 10),
                1);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoMapper.toResponse(aluno)).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.buscarPorId(1L);

        verify(alunoRepository).findById(1L);
        verify(alunoMapper).toResponse(aluno);
        verifyNoMoreInteractions(alunoRepository, alunoMapper);
        assertEquals(responseEsperado, response);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarAlunoNaoExistir() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> alunoService.buscarPorId(99L));

        verify(alunoRepository).findById(99L);
        verifyNoMoreInteractions(alunoRepository);
        verifyNoInteractions(alunoMapper);
        assertEquals("Aluno com ID 99 não encontrado.", exception.getMessage());
    }

    @Test
    void deveAtualizarAlunoQuandoDadosForemValidos() {
        Aluno alunoExistente = criarAluno(1L);
        AlunoRequest request = new AlunoRequest(
                "Gabriel Vitor de Souza",
                "111.444.777-35",
                "01310-100",
                LocalDate.of(1999, 1, 20),
                LocalDate.of(2026, 9, 15),
                2);
        AlunoResponse responseEsperado = criarResponse(
                1L,
                "Gabriel Vitor de Souza",
                "11144477735",
                "01310100",
                LocalDate.of(1999, 1, 20),
                LocalDate.of(2026, 9, 15),
                2);

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoExistente));
        when(alunoRepository.save(alunoExistente)).thenReturn(alunoExistente);
        when(alunoMapper.toResponse(alunoExistente)).thenReturn(responseEsperado);

        AlunoResponse response = alunoService.atualizar(1L, request);

        verify(alunoRepository).findById(1L);
        verify(alunoMapper).preencherAluno(eq(request), same(alunoExistente));
        verify(alunoRepository).save(same(alunoExistente));
        verify(alunoMapper).toResponse(alunoExistente);
        verifyNoMoreInteractions(alunoRepository, alunoMapper);

        assertEquals(responseEsperado, response);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarAlunoNaoExistir() {
        AlunoRequest request = criarRequestValidoParaPrimeiroSemestre();
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> alunoService.atualizar(1L, request));

        verify(alunoRepository).findById(1L);
        verify(alunoRepository, never()).save(any(Aluno.class));
        verifyNoInteractions(alunoMapper);
        assertEquals("Aluno com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void deveExcluirAlunoQuandoExistir() {
        Aluno aluno = criarAluno(1L);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        alunoService.excluir(1L);

        verify(alunoRepository).findById(1L);
        verify(alunoRepository).delete(aluno);
        verifyNoMoreInteractions(alunoRepository);
        verifyNoInteractions(alunoMapper);
    }

    @Test
    void deveLancarExcecaoQuandoExcluirAlunoNaoExistir() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException exception = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> alunoService.excluir(1L));

        verify(alunoRepository).findById(1L);
        verify(alunoRepository, never()).delete(any(Aluno.class));
        verifyNoInteractions(alunoMapper);
        assertEquals("Aluno com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoDataNascimentoForDepoisDaMatricula() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Data Invalida",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(2026, 5, 10),
                LocalDate.of(2026, 2, 10),
                1);

        assertRegraDeNegocio(request, "DATA_NASCIMENTO_INVALIDA", "dataNascimento",
                "A data de nascimento não pode ser posterior à data da matrícula.");
    }

    @Test
    void deveLancarExcecaoQuandoAlunoForMenorDeIdade() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Menor",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(2010, 1, 1),
                LocalDate.of(2026, 2, 10),
                1);

        assertRegraDeNegocio(request, "IDADE_MINIMA_NAO_ATENDIDA", "dataNascimento",
                "O aluno deve ter, no mínimo, 18 anos na data da matrícula.");
    }

    @Test
    void deveLancarExcecaoQuandoCpfForInvalido() {
        AlunoRequest request = new AlunoRequest(
                "Aluno CPF Invalido",
                "111.111.111-11",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 2, 10),
                1);

        assertRegraDeNegocio(request, "CPF_INVALIDO", "cpf", "O CPF informado é inválido.");
    }

    @Test
    void deveLancarExcecaoQuandoCepForInvalido() {
        AlunoRequest request = new AlunoRequest(
                "Aluno CEP Invalido",
                "529.982.247-25",
                "1234",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 2, 10),
                1);

        assertRegraDeNegocio(request, "CEP_INVALIDO", "cep", "O CEP informado é inválido.");
    }

    @Test
    void deveLancarExcecaoQuandoSemestreForInvalido() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Semestre Invalido",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 2, 10),
                3);

        assertRegraDeNegocio(request, "SEMESTRE_INVALIDO", "semestre", "O semestre informado deve ser 1 ou 2.");
    }

    @Test
    void deveLancarExcecaoQuandoSemestreForNulo() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Semestre Nulo",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 2, 10),
                null);

        assertRegraDeNegocio(request, "SEMESTRE_INVALIDO", "semestre", "O semestre informado deve ser 1 ou 2.");
    }

    @Test
    void deveLancarExcecaoQuandoPrimeiroSemestreEstiverForaDoPrazo() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Primeiro Semestre",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 4, 10),
                1);

        assertRegraDeNegocio(request, "PRAZO_PRIMEIRO_SEMESTRE_ENCERRADO", "dataMatricula",
                "A matrícula do primeiro semestre deve ser realizada até março.");
    }

    @Test
    void deveLancarExcecaoQuandoSegundoSemestreEstiverForaDoPrazo() {
        AlunoRequest request = new AlunoRequest(
                "Aluno Segundo Semestre",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(1998, 1, 1),
                LocalDate.of(2026, 10, 10),
                2);

        assertRegraDeNegocio(request, "PRAZO_SEGUNDO_SEMESTRE_ENCERRADO", "dataMatricula",
                "A matrícula do segundo semestre deve ser realizada até setembro.");
    }

    private void assertRegraDeNegocio(AlunoRequest request, String codigo, String campo, String mensagem) {
        ViolacaoRegraDeNegocioException exception = assertThrows(
                ViolacaoRegraDeNegocioException.class,
                () -> alunoService.cadastrar(request));

        verifyNoInteractions(alunoRepository, alunoMapper);
        assertAll(
                () -> assertEquals(codigo, exception.getCodigo()),
                () -> assertEquals(campo, exception.getCampo()),
                () -> assertEquals(mensagem, exception.getMessage()));
    }

    private AlunoRequest criarRequestValidoParaPrimeiroSemestre() {
        return new AlunoRequest(
                "Gabriel Vitor Martins",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(2000, 5, 10),
                LocalDate.of(2026, 2, 10),
                1);
    }

    private AlunoResponse criarResponse(Long id, String nomeCompleto, String cpf, String cep,
            LocalDate dataNascimento, LocalDate dataMatricula, Integer semestre) {
        return new AlunoResponse(id, nomeCompleto, cpf, cep, dataNascimento, dataMatricula, semestre);
    }

    private Aluno criarAluno(Long id) {
        Aluno aluno = new Aluno();
        aluno.setId(id);
        return aluno;
    }
}
