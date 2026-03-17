package pds.umulingo.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import pds.umulingo.domain.ports.input.CursoService;
import pds.umulingo.domain.ports.input.InscripcionService;
import pds.umulingo.domain.ports.input.UsuarioService;
import pds.umulingo.domain.ports.input.dto.InscripcionDTO;
import pds.umulingo.domain.ports.input.dto.InscripcionesUsuarioDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestDTO;

/**
 * 
 */
@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
public class InscripcionServiceImplTest {

	@Autowired
	private CursoService cursoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private InscripcionService inscripcionService;

	private String usuarioId;

	private String cursoId;
	
	@BeforeAll
	void setUp() {
		var usuario = usuarioService.registrarUsuario("j@um.es", "Jesús");
		this.usuarioId = usuario.id();
		
		var curso = cursoService.crearCurso("ingles-basico", "Inglés Básico");
		this.cursoId = curso.id();
        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "¿Cómo se dice 'gato' en inglés?",
                List.of("Dog", "Cat", "Bird", "Fish"),
                1));
	}
	
	@Test
	@DisplayName("Inscripción existe después de hacer una inscripción")
	void testInscripcionSimple() {
		InscripcionDTO inscripcion = inscripcionService.inscribirUsuario(usuarioId, cursoId);
		
		InscripcionesUsuarioDTO resultado = inscripcionService.cursosInscrito(usuarioId);
		
		assertEquals(1, resultado.inscripciones().size());
		assertEquals(inscripcion.cursoId(), resultado.inscripciones().getFirst().cursoId());
		assertEquals("Inglés Básico", resultado.inscripciones().getFirst().nombre());
	}
	
	@Test
	@DisplayName("Inscripción inválida porque usuario no existe")
	void testInscripcionNoValid_UsuarioNoExiste() {
		assertThrows(IllegalArgumentException.class, () -> {
			inscripcionService.inscribirUsuario("inventado", cursoId);
		});
	}
		
}
