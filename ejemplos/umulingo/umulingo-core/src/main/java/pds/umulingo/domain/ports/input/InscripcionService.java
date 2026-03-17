package pds.umulingo.domain.ports.input;

import pds.umulingo.domain.ports.input.dto.InscripcionDTO;
import pds.umulingo.domain.ports.input.dto.InscripcionesUsuarioDTO;

public interface InscripcionService {

	InscripcionDTO inscribirUsuario(String usuarioId, String cursoId);

	InscripcionesUsuarioDTO cursosInscrito(String usuarioId);

}
