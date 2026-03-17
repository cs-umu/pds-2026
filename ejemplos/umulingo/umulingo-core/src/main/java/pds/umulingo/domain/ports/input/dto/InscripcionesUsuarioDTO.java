package pds.umulingo.domain.ports.input.dto;

import java.util.List;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

public record InscripcionesUsuarioDTO(String usuarioId, List<CursoInscritoDTO> inscripciones) {

	public InscripcionesUsuarioDTO(UsuarioId usuarioId, List<CursoInscritoDTO> inscripcionesUsuario) {
		this(usuarioId.valor(), inscripcionesUsuario);
	}

	public static record CursoInscritoDTO(String cursoId, String nombre) {

		public CursoInscritoDTO(CursoId id, String nombre) {
			this(id.valor(), nombre);
		} 
	}
	
}
