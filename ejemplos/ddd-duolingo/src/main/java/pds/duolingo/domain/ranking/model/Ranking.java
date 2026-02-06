package pds.duolingo.domain.ranking.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import pds.duolingo.domain.usuario.id.UsuarioId;

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
		PosicionUsuario posicion = posiciones.stream()
				.filter(p -> p.getUsuarioId().equals(usuarioId))
				.findFirst()
				.orElseGet(() -> {
					PosicionUsuario nueva = new PosicionUsuario(usuarioId);
					posiciones.add(nueva);
					return nueva;
				});
		posicion.incrementarCorrectas();
		return posicion;
	}

	public List<PosicionUsuario> obtenerPosiciones() {
		return posiciones.stream()
				.sorted(Comparator.comparingInt(PosicionUsuario::getRespuestasCorrectas).reversed())
				.toList();
	}

	public Optional<PosicionUsuario> obtenerPosicion(UsuarioId usuarioId) {
		return posiciones.stream()
				.filter(p -> p.getUsuarioId().equals(usuarioId))
				.findFirst();
	}
}
