package pds.umulingo.adapters.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Entity
public class PosicionUsuarioEntity {
	
	@Id
	private String usuario;

	private int puntos;
	
	public PosicionUsuarioEntity() { }
	
	public PosicionUsuarioEntity(UsuarioId usuario, int puntos) {
		this.usuario = usuario.valor();
		this.puntos = puntos;
	}
	
	public UsuarioId getUsuario() {
		return new UsuarioId(usuario);
	}
	
	public int getPuntos() {
		return puntos;
	}
}
