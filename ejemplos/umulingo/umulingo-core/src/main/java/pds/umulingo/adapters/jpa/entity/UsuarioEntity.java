package pds.umulingo.adapters.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Entity
public class UsuarioEntity {
	@Id
	private String id;
	
	private String email;

	/**
	 * cascade porque el ID se está generando automáticamente (si no, JPA cree el objeto ya existe en BD)
	 * orphanRemoval porque Inscripción está subordinada a Usuario (agregación fuerte)
	 */
	@OneToMany(//mappedBy = "usuario",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<InscripcionEntity> inscripciones;

	private String nombre;

	public UsuarioEntity() { }
	
	public UsuarioEntity(UsuarioId id, Email email, String nombre, List<InscripcionEntity> inscripciones) {
		this.id = id.valor();
		this.email = email.value();
		this.nombre = nombre;
		this.inscripciones = new ArrayList<>(inscripciones);
	}
	
	public UsuarioId getId() {
		return new UsuarioId(id);
	}
	
	public Email getEmail() {
		return new Email(email);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public List<? extends InscripcionEntity> getInscripciones() {
		return inscripciones;
	}

}
