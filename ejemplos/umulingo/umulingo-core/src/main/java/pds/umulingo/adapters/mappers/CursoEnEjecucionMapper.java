package pds.umulingo.adapters.mappers;

import org.springframework.stereotype.Component;

import pds.umulingo.adapters.jpa.entity.CursoEnEjecucionEntity;
import pds.umulingo.domain.model.cursoenejecucion.model.CursoEnEjecucion;

@Component
public class CursoEnEjecucionMapper {

	public CursoEnEjecucionEntity toEntity(CursoEnEjecucion ejecucion) {
		// new CursoEnEjecucionEntity(ejecucion.getId(), ejecucion.getCursoId(), ejecucion.getUsuarioId(), ejecucion.getEstado());
		var entity = ejecucion.toEntity((id, usuarioId, cursoId, preguntaActual) -> new CursoEnEjecucionEntity(id, cursoId, usuarioId, preguntaActual));
		// var entity = ejecucion.toEntity(CursoEnEjecucionEntity::new);
		return (CursoEnEjecucionEntity) entity;
	}
	

	public CursoEnEjecucion toModel(CursoEnEjecucionEntity entity) {
		return CursoEnEjecucion.fromEntity(entity.getId(), entity.getCursoId(), entity.getUsuarioId(), entity.getEstadoProgreso());
	}

}
