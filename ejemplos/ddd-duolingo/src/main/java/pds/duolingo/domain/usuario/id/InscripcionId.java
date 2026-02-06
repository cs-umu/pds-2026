package pds.duolingo.domain.usuario.id;

import java.util.UUID;

public record InscripcionId(String valor) {
    public InscripcionId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("InscripcionId no puede ser nulo o vacío");
        }
    }

    public static InscripcionId nuevo() {
        return new InscripcionId(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return valor;
    }
}
