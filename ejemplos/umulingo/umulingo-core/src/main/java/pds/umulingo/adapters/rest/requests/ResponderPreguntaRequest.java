package pds.umulingo.adapters.rest.requests;

import java.util.List;

public record ResponderPreguntaRequest(List<Object> respuestas, TipoRespuesta tipo) {

	public static enum TipoRespuesta {
		HUECOS,
		OPCION_SIMPLE,
		TEXTO
	}
		
}
