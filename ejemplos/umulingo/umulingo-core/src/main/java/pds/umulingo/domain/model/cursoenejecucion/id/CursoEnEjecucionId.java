package pds.umulingo.domain.model.cursoenejecucion.id;

import java.util.UUID;

public record CursoEnEjecucionId(String valor) {
    public CursoEnEjecucionId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("ProgresoId no puede ser nulo o vacío");
        }
    }

    public static CursoEnEjecucionId nuevo() {
        return new CursoEnEjecucionId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return valor;
    }
}
