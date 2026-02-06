package pds.duolingo.domain.curso.model;

import java.util.List;

public class RespuestaHuecos extends Respuesta {
    private final List<String> huecosCompletados;

    public RespuestaHuecos(List<String> huecosCompletados) {
        this.huecosCompletados = List.copyOf(huecosCompletados);
    }

    public List<String> getHuecosCompletados() {
        return huecosCompletados;
    }
}
