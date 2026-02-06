package pds.duolingo.domain.cursoenejecucion.repository;

import java.util.Optional;

import pds.duolingo.domain.cursoenejecucion.id.CursoEnEjecucionId;
import pds.duolingo.domain.cursoenejecucion.model.CursoEnEjecucion;

public interface CursoEnEjecucionRepository {
    void save(CursoEnEjecucion progreso);
    Optional<CursoEnEjecucion> findById(CursoEnEjecucionId id);
    void eliminar(CursoEnEjecucionId id);
}
