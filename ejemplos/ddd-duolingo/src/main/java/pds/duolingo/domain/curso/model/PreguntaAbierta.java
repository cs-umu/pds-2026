package pds.duolingo.domain.curso.model;

import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;

public class PreguntaAbierta extends Pregunta {
    private final String respuestaCorrecta;

    public PreguntaAbierta(PreguntaId id, String enunciado, String respuestaCorrecta) {
        super(id, enunciado);
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    @Override
    public boolean verificarRespuesta(Respuesta respuesta) {
        if (!(respuesta instanceof RespuestaTexto)) {
            return false;
        }
        RespuestaTexto respuestaTexto = (RespuestaTexto) respuesta;
        return respuestaCorrecta.equalsIgnoreCase(respuestaTexto.getTexto().trim());
    }
}
