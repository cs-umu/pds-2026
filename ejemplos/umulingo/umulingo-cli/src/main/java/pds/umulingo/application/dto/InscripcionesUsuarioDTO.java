package pds.umulingo.application.dto;

import java.util.List;

public record InscripcionesUsuarioDTO(String usuarioId, List<CursoInscritoDTO> inscripciones) {

	public static record CursoInscritoDTO(String cursoId, String nombre) { }
	
}
