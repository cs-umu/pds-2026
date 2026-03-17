package pds.umulingo.adapters.jpa.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Entity
public class CursoEnEjecucionEntity {

	@Id
	@Embedded
	@AttributeOverride(name = "valor", column = @Column(name = "id"))
	private CursoEnEjecucionId id;

	@Embedded
	@AttributeOverride(name = "valor", column = @Column(name = "curso_id"))
	private CursoId cursoId;

	@Embedded
	@AttributeOverride(name = "valor", column = @Column(name = "usuario_id"))
	private UsuarioId usuarioId;

	private int estadoProgreso;

	/** Para JPA */
	public CursoEnEjecucionEntity() { }
	
	public CursoEnEjecucionEntity(CursoEnEjecucionId id, CursoId cursoId, UsuarioId usuarioId, int pregunta) {
		this.id = id;
		this.cursoId = cursoId;
		this.usuarioId = usuarioId;
		this.estadoProgreso = pregunta;
	}
	
	public CursoEnEjecucionId getId() {
		return id;
	}
	
	
	public UsuarioId getUsuarioId() {
		return usuarioId;
	}
	
	public CursoId getCursoId() {
		return cursoId;
	}

	public int getEstadoProgreso() {
		return estadoProgreso;
	}
}
