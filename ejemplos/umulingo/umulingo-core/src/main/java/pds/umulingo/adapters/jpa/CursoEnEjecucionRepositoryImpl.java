package pds.umulingo.adapters.jpa;

import java.util.Optional;

import org.springframework.stereotype.Component;

import pds.umulingo.adapters.jpa.entity.CursoEnEjecucionEntity;
import pds.umulingo.adapters.jpa.repository.CursoEnEjecucionRepositoryJPA;
import pds.umulingo.adapters.mappers.CursoEnEjecucionMapper;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.cursoenejecucion.model.CursoEnEjecucion;
import pds.umulingo.domain.model.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Component
public class CursoEnEjecucionRepositoryImpl implements CursoEnEjecucionRepository {
	private CursoEnEjecucionRepositoryJPA jpa;
	private CursoEnEjecucionMapper mapper;
	
	public CursoEnEjecucionRepositoryImpl(CursoEnEjecucionRepositoryJPA jpa, CursoEnEjecucionMapper mapper) {
		this.jpa = jpa;
		this.mapper = mapper;
	}
	
	
	@Override
	public void save(CursoEnEjecucion ejecucion) {
		CursoEnEjecucionEntity entity = mapper.toEntity(ejecucion);
		jpa.save(entity);
	}

	@Override
	public Optional<CursoEnEjecucion> findById(CursoEnEjecucionId id) {
		Optional<CursoEnEjecucionEntity> r = jpa.findById(id);
		return r.map(mapper::toModel);
	}

	@Override
	public void eliminar(CursoEnEjecucionId id) {
		jpa.deleteById(id);
	}


	@Override
	public Optional<CursoEnEjecucion> findByUsuarioCurso(UsuarioId usuarioId, CursoId cursoId) {
		Optional<CursoEnEjecucionEntity> r = jpa.findByUsuarioCurso(usuarioId, cursoId);
		return r.map(mapper::toModel);
	}

}
