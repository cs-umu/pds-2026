package pds.umulingo.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import pds.umulingo.domain.ports.input.CursoService;
import pds.umulingo.domain.ports.input.EjecucionDeCursoService;
import pds.umulingo.domain.ports.input.InscripcionService;
import pds.umulingo.domain.ports.input.RankingService;
import pds.umulingo.domain.ports.input.UsuarioService;
import pds.umulingo.domain.ports.input.dto.InscripcionDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestDTO;
import pds.umulingo.domain.ports.input.dto.ProgresoDTO;
import pds.umulingo.domain.ports.input.dto.RankingInfoDTO;
import pds.umulingo.domain.ports.input.dto.RespuestaDTO.RespuestaOpcionDTO;

@SpringBootTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class EjecucionDeCursoServiceImplTest {


	@Autowired
	private CursoService cursoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private InscripcionService inscripcionService;

	@Autowired
	private EjecucionDeCursoService ejecucionService;

	@Autowired
	private RankingService rankingService;

	
	private String usuarioId;

	private String cursoId;

	private InscripcionDTO inscripcion;
	
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
	void testEjecucionConRanking() {
		// jesusc: Por qué esto no se puede poner en setUp
		// Error: 	org.hibernate.LazyInitializationException: Cannot lazily initialize collection of role 'pds.umulingo.adapters.jpa.entity.UsuarioEntity.inscripciones' with key 'j@um.es' (no session)
		inscripcionService.inscribirUsuario(usuarioId, cursoId);

		ProgresoDTO progreso1 = ejecucionService.iniciarCurso(usuarioId, cursoId);
		
		boolean resultado = ejecucionService.responderPregunta(progreso1.id(), new RespuestaOpcionDTO(1));
		assertTrue(resultado);
		
		RankingInfoDTO ranking = rankingService.rankingGlobal();
		assertEquals(1, ranking.posiciones().size());
		assertEquals("Jesús", ranking.posiciones().getFirst().nombre());
		assertEquals(1, ranking.posiciones().getFirst().puntos());
	}

}
