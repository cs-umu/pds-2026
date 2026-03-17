package pds.umulingo.domain.ports.input;

import pds.umulingo.domain.ports.input.dto.CursoDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaDTO;

public interface CursoService {

	CursoDTO crearCurso(String id, String nombre);

	int agregarPregunta(String cursoId, PreguntaDTO dto);

}
