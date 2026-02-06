package pds.duolingo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pds.duolingo.application.dto.UsuarioDTO;
import pds.duolingo.infrastructure.InMemoryUsuarioRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private InMemoryUsuarioRepository usuarioRepository;
    private final String emailJuan = "juan@example.com";
    private final String emailMaria = "maria@example.com";


    @BeforeEach
    void setUp() {
        usuarioRepository = new InMemoryUsuarioRepository();
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void registrarUsuarioGuardaEnRepositorio() {
        UsuarioDTO usuario = usuarioService.registrarUsuario(emailJuan, "Juan");

        assertNotNull(usuario.id());
        assertEquals("Juan", usuario.nombre());
        assertEquals("juan@example.com", usuario.email());
        assertTrue(usuarioService.obtenerUsuario(usuario.id()).isPresent());
    }

    @Test
    void registrarUsuarioConEmailDuplicadoLanzaExcepcion() {
        usuarioService.registrarUsuario(emailJuan, "Juan");

        assertThrows(IllegalArgumentException.class, () ->
            usuarioService.registrarUsuario(emailJuan, "Otro Juan")
        );
    }

    @Test
    void obtenerUsuarioPorEmail() {
        UsuarioDTO usuario = usuarioService.registrarUsuario(emailMaria, "María");

        UsuarioDTO encontrado = usuarioService.obtenerUsuarioPorEmail(emailMaria).get();

        assertEquals(usuario.id(), encontrado.id());
    }

    @Test
    void obtenerTodosLosUsuarios() {
        usuarioService.registrarUsuario(emailJuan, "Juan");
        usuarioService.registrarUsuario(emailMaria, "María");

        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

        assertEquals(2, usuarios.size());
    }

    @Test
    void eliminarUsuario() {
        UsuarioDTO usuario = usuarioService.registrarUsuario(emailJuan, "Juan");

        usuarioService.eliminarUsuario(usuario.id());

        assertFalse(usuarioService.obtenerUsuario(usuario.id()).isPresent());
    }
}
