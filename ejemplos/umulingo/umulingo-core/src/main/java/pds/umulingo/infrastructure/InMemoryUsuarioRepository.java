package pds.umulingo.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;

public class InMemoryUsuarioRepository implements UsuarioRepository {
    private final Map<UsuarioId, Usuario> usuarios = new HashMap<>();

    @Override
    public void save(Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> findById(UsuarioId id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(Email email) {
        return usuarios.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void eliminar(UsuarioId id) {
        usuarios.remove(id);
    }
}
