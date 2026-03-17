package pds.umulingo.domain.model.ranking.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import pds.umulingo.domain.model.usuario.id.UsuarioId;

/**
 * Representa el ranking de usuarios.
 */
public class Ranking {
	private final List<PosicionUsuario> posiciones;

	public Ranking() {
		this.posiciones = new ArrayList<>();
	}
	
	public Ranking(Collection<? extends PosicionUsuario> posiciones) {
		this.posiciones = new ArrayList<>(posiciones);
	}

	public PosicionUsuario registrarRespuestaCorrecta(UsuarioId usuarioId) {
		PosicionUsuario posicionEncontrada = null;
		for (PosicionUsuario posicionUsuario : posiciones) {
			if (posicionUsuario.getUsuarioId().equals(usuarioId)) {
				posicionEncontrada = posicionUsuario;
				break;
			}
		}
		
		if (posicionEncontrada == null) {
			posicionEncontrada = new PosicionUsuario(usuarioId); 
			posiciones.add(posicionEncontrada);
		}
		
		posicionEncontrada.incrementarCorrectas();
		return posicionEncontrada;
	}

	public List<PosicionUsuario> getPosicionesOrdenadas() {
		return posiciones.stream()
				.sorted(Comparator.comparingInt(PosicionUsuario::getRespuestasCorrectas).reversed())
				.toList();
	}

	public Optional<PosicionUsuario> posicionDe(UsuarioId usuarioId) {
		return posiciones.stream()
				.filter(p -> p.getUsuarioId().equals(usuarioId))
				.findFirst();
	}

}
