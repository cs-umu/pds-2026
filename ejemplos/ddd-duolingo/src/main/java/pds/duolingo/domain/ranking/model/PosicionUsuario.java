package pds.duolingo.domain.ranking.model;

import pds.duolingo.domain.usuario.id.UsuarioId;

/**
 * Representa un usuario junto con los datos relativos al ranking.
 */
public class PosicionUsuario {
	private final UsuarioId usuarioId;
	private int respuestasCorrectas;

	public PosicionUsuario(UsuarioId usuarioId) {
		this.usuarioId = usuarioId;
		this.respuestasCorrectas = 0;
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
