package pds.umulingo.adapters.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pds.umulingo.adapters.jpa.entity.CursoEnEjecucionEntity;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Repository
public interface CursoEnEjecucionRepositoryJPA extends JpaRepository<CursoEnEjecucionEntity, CursoEnEjecucionId> {

	
	@Query("select e from CursoEnEjecucionEntity e where usuarioId = ?1 and cursoId = ?2")
	Optional<CursoEnEjecucionEntity> findByUsuarioCurso(UsuarioId usuarioId, CursoId cursoId);

}
