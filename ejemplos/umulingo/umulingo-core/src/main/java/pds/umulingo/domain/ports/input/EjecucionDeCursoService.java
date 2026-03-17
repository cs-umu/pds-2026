package pds.umulingo.domain.ports.input;

import java.util.Optional;

import pds.umulingo.domain.ports.input.dto.PreguntaPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.ProgresoDTO;
import pds.umulingo.domain.ports.input.dto.RespuestaDTO;

public interface EjecucionDeCursoService {

	/**
	 * Inicia un nuevo curso para un usuario. Si el usuario ya está inscrito
	 * en el curso, entonces reanuda el curso.
	 * 
	 * @param usuarioId
	 * @param cursoId
	 * @return
	 */
	ProgresoDTO iniciarCurso(String usuarioId, String cursoId);

	/**
	 * Dado el progreso del curso devuelve la información sobre la siguiente 
	 * pregunta para que se pueda mostrar al usuario.
	 */
	Optional<PreguntaPresentacionDTO> presentarPregunta(String progresoId);

	/**
	 * Procesa la respuesta a una pregunta y devuelve true si es correcta.
	 * Cambia el estado del curso y lo avanza a la siguiente pregunta.
	 */
	boolean responderPregunta(String progresoId, RespuestaDTO respuesta);

}
