package br.com.prova1qa.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.prova1qa.api.model.Aluno;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
