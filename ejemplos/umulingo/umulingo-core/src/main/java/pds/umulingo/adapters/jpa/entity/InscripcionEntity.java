package pds.umulingo.adapters.jpa.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.usuario.id.InscripcionId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Entity
public class InscripcionEntity {

	@Id
	private String id;

	@Embedded
	@AttributeOverride(name = "valor", column = @Column(name = "usuario_id"))
	private UsuarioId usuarioId;
	
	@Embedded
	@AttributeOverride(name = "valor", column = @Column(name = "curso_id"))
	private CursoId cursoId;

	public InscripcionEntity() { }
	
	public InscripcionEntity(InscripcionId id, UsuarioId usuarioId, CursoId cursoId) {
		this.id = id.valor();
		this.usuarioId = usuarioId;
		this.cursoId = cursoId;
	}
	
	public InscripcionId getId() {
		return new InscripcionId(id);
	}
	
	public UsuarioId getUsuarioId() {
		return usuarioId;
	}
	
	public CursoId getCursoId() {
		return cursoId;
	}

}
