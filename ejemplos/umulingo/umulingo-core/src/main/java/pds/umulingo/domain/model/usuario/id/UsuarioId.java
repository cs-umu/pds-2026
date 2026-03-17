package pds.umulingo.domain.model.usuario.id;

public record UsuarioId(String valor) {
    public UsuarioId {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("UsuarioId no puede ser nulo o vacío");
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
