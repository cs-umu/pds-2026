package pds.duolingo.domain.curso.id;

import java.util.UUID;

public record CursoId(String valor) {
    public CursoId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("CursoId no puede ser nulo o vacío");
        }
    }

    public static CursoId nuevo() {
        return new CursoId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return valor;
    }
}
