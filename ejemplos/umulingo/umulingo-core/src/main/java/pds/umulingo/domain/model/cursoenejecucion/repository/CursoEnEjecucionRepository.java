package pds.umulingo.domain.model.cursoenejecucion.repository;

import java.util.Optional;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.cursoenejecucion.model.CursoEnEjecucion;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

public interface CursoEnEjecucionRepository {
    void save(CursoEnEjecucion progreso);
    
    Optional<CursoEnEjecucion> findById(CursoEnEjecucionId id);
    Optional<CursoEnEjecucion> findByUsuarioCurso(UsuarioId usuarioId, CursoId cursoId);

    void eliminar(CursoEnEjecucionId id);
}
