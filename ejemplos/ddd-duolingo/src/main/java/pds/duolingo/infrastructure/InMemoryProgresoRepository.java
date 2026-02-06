package pds.duolingo.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import pds.duolingo.domain.cursoenejecucion.id.CursoEnEjecucionId;
import pds.duolingo.domain.cursoenejecucion.model.CursoEnEjecucion;
import pds.duolingo.domain.cursoenejecucion.repository.CursoEnEjecucionRepository;

public class InMemoryProgresoRepository implements CursoEnEjecucionRepository {
    private final Map<CursoEnEjecucionId, CursoEnEjecucion> progresos = new HashMap<>();

    @Override
    public void save(CursoEnEjecucion progreso) {
        progresos.put(progreso.getId(), progreso);
    }

    @Override
    public Optional<CursoEnEjecucion> findById(CursoEnEjecucionId id) {
        return Optional.ofNullable(progresos.get(id));
    }

    @Override
    public void eliminar(CursoEnEjecucionId id) {
        progresos.remove(id);
    }
}
