package pds.umulingo.domain.model.curso.id;

public record CursoId(String valor) {
    public CursoId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("CursoId no puede ser nulo o vacío");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
