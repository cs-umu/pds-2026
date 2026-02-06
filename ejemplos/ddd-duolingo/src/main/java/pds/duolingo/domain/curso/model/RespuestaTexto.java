package pds.duolingo.domain.curso.model;

public class RespuestaTexto extends Respuesta {
    private final String texto;

    public RespuestaTexto(String texto) {
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }
}
