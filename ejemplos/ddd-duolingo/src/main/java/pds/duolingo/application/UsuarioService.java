package pds.duolingo.application;

import java.util.List;
import java.util.Optional;

import pds.duolingo.application.dto.UsuarioDTO;
import pds.duolingo.domain.usuario.Email;
import pds.duolingo.domain.usuario.id.UsuarioId;
import pds.duolingo.domain.usuario.model.Usuario;
import pds.duolingo.domain.usuario.repository.UsuarioRepository;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioDTO registrarUsuario(String email, String nombre) {
        Email emailVO = new Email(email);
        if (usuarioRepository.buscarPorEmail(emailVO).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + email);
        }
        Usuario usuario = new Usuario(emailVO, nombre);
        usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    public Optional<UsuarioDTO> obtenerUsuario(String usuarioId) {
        return usuarioRepository.findById(new UsuarioId(usuarioId)).map(this::toDTO);
    }

    public Optional<UsuarioDTO> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(new Email(email)).map(this::toDTO);
    }

    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.buscarTodos().stream().map(this::toDTO).toList();
    }

    public void eliminarUsuario(String usuarioId) {
        usuarioRepository.eliminar(new UsuarioId(usuarioId));
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId().valor(),
            usuario.getEmail().value(),
            usuario.getNombre()
        );
    }
}
