package pds.umulingo.domain.model.curso.repository;

import java.util.List;

import org.jspecify.annotations.Nullable;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;

public interface CursoRepository {
    void save(Curso curso);
    
    @Nullable
    Curso findById(CursoId id);
    
    List<Curso> buscarTodos();
    void eliminar(CursoId id);
}
