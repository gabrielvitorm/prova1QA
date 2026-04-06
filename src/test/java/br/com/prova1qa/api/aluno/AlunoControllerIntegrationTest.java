package br.com.prova1qa.api.aluno;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import br.com.prova1qa.api.repository.AlunoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AlunoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AlunoRepository alunoRepository;

    @BeforeEach
    void setUp() {
        alunoRepository.deleteAll();
    }

    @Test
    void deveCadastrarBuscarAtualizarEExcluirAluno() throws Exception {
        String cadastro = criarAlunoJson(
                "Gabriel Vitor Martins",
                "529.982.247-25",
                "01001-000",
                LocalDate.of(2026, 4, 10),
                2,
                LocalDate.of(2000, 5, 10));

        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cadastro))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCompleto").value("Gabriel Vitor Martins"))
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.cep").value("01001000"))
                .andExpect(jsonPath("$.dataMatricula").value("2026-04-10"))
                .andExpect(jsonPath("$.semestre").value(2));

        Long id = alunoRepository.findAll().get(0).getId();

        mockMvc.perform(get("/alunos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.cep").value("01001000"))
                .andExpect(jsonPath("$.dataMatricula").value("2026-04-10"));

        String atualizacao = criarAlunoJson(
                "Gabriel Vitor de Souza",
                "111.444.777-35",
                "01310-100",
                LocalDate.of(2026, 4, 15),
                2,
                LocalDate.of(1999, 1, 20));

        mockMvc.perform(put("/alunos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(atualizacao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Gabriel Vitor de Souza"))
                .andExpect(jsonPath("$.cpf").value("11144477735"))
                .andExpect(jsonPath("$.cep").value("01310100"))
                .andExpect(jsonPath("$.dataMatricula").value("2026-04-15"));

        mockMvc.perform(delete("/alunos/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/alunos/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveImpedirCadastroDeMenorDeIdade() throws Exception {
        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarAlunoJson(
                                "Aluno Menor",
                                "529.982.247-25",
                                "01001-000",
                                LocalDate.of(2026, 4, 10),
                                2,
                                LocalDate.of(2010, 1, 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ALUNO_MENOR_DE_IDADE"))
                .andExpect(jsonPath("$.fieldErrors.dataNascimento").value("Aluno precisa ter pelo menos 18 anos"))
                .andExpect(jsonPath("$.message").value("Aluno precisa ter pelo menos 18 anos"));
    }

    @Test
    void deveValidarCpf() throws Exception {
        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarAlunoJson(
                                "Aluno CPF Invalido",
                                "111.111.111-11",
                                "01001-000",
                                LocalDate.of(2026, 4, 10),
                                2,
                                LocalDate.of(1998, 1, 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("CPF_INVALIDO"))
                .andExpect(jsonPath("$.fieldErrors.cpf").value("CPF invalido"))
                .andExpect(jsonPath("$.message").value("CPF invalido"));
    }

    @Test
    void deveValidarCep() throws Exception {
        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarAlunoJson(
                                "Aluno CEP Invalido",
                                "529.982.247-25",
                                "1234",
                                LocalDate.of(2026, 4, 10),
                                2,
                                LocalDate.of(1998, 1, 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("CEP_INVALIDO"))
                .andExpect(jsonPath("$.fieldErrors.cep").value("CEP invalido"))
                .andExpect(jsonPath("$.message").value("CEP invalido"));
    }

    @Test
    void deveBloquearPrimeiroSemestreDepoisDeMarco() throws Exception {
        mockMvc.perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(criarAlunoJson(
                                "Aluno Primeiro Semestre",
                                "529.982.247-25",
                                "01001-000",
                                LocalDate.of(2026, 4, 10),
                                1,
                                LocalDate.of(1998, 1, 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PRAZO_MATRICULA_1_SEMESTRE_ENCERRADO"))
                .andExpect(jsonPath("$.fieldErrors.dataMatricula").value("Matricula do primeiro semestre so pode ser feita ate marco"))
                .andExpect(jsonPath("$.message").value("Matricula do primeiro semestre so pode ser feita ate marco"));
    }

    private String criarAlunoJson(String nome, String cpf, String cep, LocalDate dataMatricula, int semestre,
            LocalDate dataNascimento) {
        return """
                {
                  "nomeCompleto": "%s",
                  "cpf": "%s",
                  "cep": "%s",
                  "dataNascimento": "%s",
                  "dataMatricula": "%s",
                  "semestre": %s
                }
                """.formatted(nome, cpf, cep, dataNascimento, dataMatricula, semestre);
    }
}
