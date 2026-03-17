package pds.umulingo.domain.model.curso.model;

public class RespuestaOpcion extends Respuesta {
    private final int opcionSeleccionada;

    public RespuestaOpcion(int opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public int getOpcionSeleccionada() {
        return opcionSeleccionada;
    }
}
