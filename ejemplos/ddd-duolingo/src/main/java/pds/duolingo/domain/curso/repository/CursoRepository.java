package pds.duolingo.domain.curso.repository;

import java.util.List;
import java.util.Optional;

import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.model.Curso;

public interface CursoRepository {
    void guardar(Curso curso);
    Optional<Curso> findById(CursoId id);
    List<Curso> buscarTodos();
    void eliminar(CursoId id);
}
