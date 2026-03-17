package pds.umulingo.infrastructure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.cursoenejecucion.model.CursoEnEjecucion;
import pds.umulingo.domain.model.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

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

	@Override
	public Optional<CursoEnEjecucion> findByUsuarioCurso(UsuarioId usuarioId, CursoId cursoId) {
		throw new UnsupportedOperationException();
	}
}
