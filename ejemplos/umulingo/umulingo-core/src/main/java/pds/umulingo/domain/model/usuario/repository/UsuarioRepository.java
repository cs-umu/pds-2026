package pds.umulingo.domain.model.usuario.repository;

import java.util.List;
import java.util.Optional;

import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Usuario;

public interface UsuarioRepository {
    void save(Usuario usuario);
    Optional<Usuario> findById(UsuarioId id);
    Optional<Usuario> buscarPorEmail(Email email);
    List<Usuario> buscarTodos();
    void eliminar(UsuarioId id);
}
