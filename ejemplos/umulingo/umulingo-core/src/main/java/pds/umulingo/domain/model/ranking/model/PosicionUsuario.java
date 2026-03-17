package pds.umulingo.domain.model.ranking.model;

import pds.umulingo.domain.model.usuario.id.UsuarioId;

/**
 * Representa un usuario junto con los datos relativos al ranking.
 */
public class PosicionUsuario {
	private final UsuarioId usuarioId;
	private int respuestasCorrectas;

	public PosicionUsuario(UsuarioId usuarioId) {
		this(usuarioId, 0);
	}
	
	public PosicionUsuario(UsuarioId usuarioId, int respuestasCorrectas) {
		this.usuarioId = usuarioId;
		this.respuestasCorrectas = respuestasCorrectas;
	}

	public UsuarioId getUsuarioId() {
		return usuarioId;
	}

	public int getRespuestasCorrectas() {
		return respuestasCorrectas;
	}

	public void incrementarCorrectas() {
		respuestasCorrectas++;
	}

}
