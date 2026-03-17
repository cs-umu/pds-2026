package pds.umulingo.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;
import pds.umulingo.domain.ports.input.UsuarioService;
import pds.umulingo.domain.ports.input.dto.UsuarioDTO;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UsuarioDTO registrarUsuario(String email, String nombre) {
        Email emailVO = new Email(email);
        if (usuarioRepository.buscarPorEmail(emailVO).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + email);
        }
        Usuario usuario = new Usuario(emailVO, nombre);
        usuarioRepository.save(usuario);
        return toDTO(usuario);
    }

    @Override
    public Optional<UsuarioDTO> obtenerUsuario(String usuarioId) {
        return usuarioRepository.findById(new UsuarioId(usuarioId)).map(this::toDTO);
    }

    @Override
    public Optional<UsuarioDTO> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(new Email(email)).map(this::toDTO);
    }

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.buscarTodos().stream().map(this::toDTO).toList();
    }

    @Override
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
