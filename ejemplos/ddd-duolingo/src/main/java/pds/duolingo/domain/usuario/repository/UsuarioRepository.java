package pds.duolingo.domain.usuario.repository;

import java.util.List;
import java.util.Optional;

import pds.duolingo.domain.usuario.Email;
import pds.duolingo.domain.usuario.id.UsuarioId;
import pds.duolingo.domain.usuario.model.Usuario;

public interface UsuarioRepository {
    void save(Usuario usuario);
    Optional<Usuario> findById(UsuarioId id);
    Optional<Usuario> buscarPorEmail(Email email);
    List<Usuario> buscarTodos();
    void eliminar(UsuarioId id);
}
