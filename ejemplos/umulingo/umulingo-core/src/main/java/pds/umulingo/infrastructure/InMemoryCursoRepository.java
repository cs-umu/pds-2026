package pds.umulingo.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.repository.CursoRepository;

public class InMemoryCursoRepository implements CursoRepository {
    private final Map<CursoId, Curso> cursos = new HashMap<>();

    @Override
    public void save(Curso curso) {
        cursos.put(curso.getId(), curso);
    }

    @Override
    public Curso findById(CursoId id) {
        return cursos.get(id);
    }

    @Override
    public List<Curso> buscarTodos() {
        return new ArrayList<>(cursos.values());
    }

    @Override
    public void eliminar(CursoId id) {
        cursos.remove(id);
    }
}
