package pds.umulingo.domain.ports.input;

import java.util.List;
import java.util.Optional;

import pds.umulingo.domain.ports.input.dto.UsuarioDTO;

public interface UsuarioService {

	UsuarioDTO registrarUsuario(String email, String nombre);

	Optional<UsuarioDTO> obtenerUsuario(String usuarioId);

	Optional<UsuarioDTO> obtenerUsuarioPorEmail(String email);

	List<UsuarioDTO> obtenerTodosLosUsuarios();

	void eliminarUsuario(String usuarioId);
}
