package pds.duolingo.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.model.Curso;
import pds.duolingo.domain.curso.repository.CursoRepository;

public class InMemoryCursoRepository implements CursoRepository {
    private final Map<CursoId, Curso> cursos = new HashMap<>();

    @Override
    public void guardar(Curso curso) {
        cursos.put(curso.getId(), curso);
    }

    @Override
    public Optional<Curso> findById(CursoId id) {
        return Optional.ofNullable(cursos.get(id));
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
