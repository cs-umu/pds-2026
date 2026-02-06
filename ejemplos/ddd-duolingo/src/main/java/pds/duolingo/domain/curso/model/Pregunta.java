package pds.duolingo.domain.curso.model;

import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;

public abstract class Pregunta {
    private final PreguntaId id;
	private final String enunciado;
    
    protected Pregunta(PreguntaId id, String enunciado) {
        this.id = id;
        this.enunciado = enunciado;
    }

    public PreguntaId getId() {
        return id;
    }
    
    public String getEnunciado() {
		return enunciado;
	}

    public abstract boolean verificarRespuesta(Respuesta respuesta);
}
