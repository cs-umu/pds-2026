package pds.duolingo.domain.curso.model;

import java.util.List;

import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;

public class PreguntaTest extends Pregunta {
    private final List<String> opciones;
    private final int opcionCorrecta;

    public PreguntaTest(PreguntaId id, String enunciado, List<String> opciones, int opcionCorrecta) {
        super(id, enunciado);
        if (opcionCorrecta < 0 || opcionCorrecta >= opciones.size()) {
            throw new IllegalArgumentException("Opción correcta fuera de rango");
        }
        this.opciones = List.copyOf(opciones);
        this.opcionCorrecta = opcionCorrecta;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public int getOpcionCorrecta() {
        return opcionCorrecta;
    }

    @Override
    public boolean verificarRespuesta(Respuesta respuesta) {
        if (!(respuesta instanceof RespuestaOpcion)) {
            return false;
        }
        RespuestaOpcion respuestaOpcion = (RespuestaOpcion) respuesta;
        return respuestaOpcion.getOpcionSeleccionada() == opcionCorrecta;
    }
}
