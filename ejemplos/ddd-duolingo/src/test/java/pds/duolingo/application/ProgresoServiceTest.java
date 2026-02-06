package pds.duolingo.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pds.duolingo.application.dto.CursoDTO;
import pds.duolingo.application.dto.InscripcionDTO;
import pds.duolingo.application.dto.PreguntaAbiertaDTO;
import pds.duolingo.application.dto.PreguntaAbiertaPresentacionDTO;
import pds.duolingo.application.dto.PreguntaPresentacionDTO;
import pds.duolingo.application.dto.ProgresoDTO;
import pds.duolingo.application.dto.RespuestaTextoDTO;
import pds.duolingo.application.dto.UsuarioDTO;
import pds.duolingo.common.events.EventBus;
import pds.duolingo.infrastructure.InMemoryCursoRepository;
import pds.duolingo.infrastructure.InMemoryProgresoRepository;
import pds.duolingo.infrastructure.InMemoryUsuarioRepository;

class ProgresoServiceTest {

    private EjecucionDeCursoService progresoService;
    private InscripcionService inscripcionService;
    private CursoService cursoService;
    private UsuarioService usuarioService;

    private InMemoryProgresoRepository progresoRepository;
    private InMemoryCursoRepository cursoRepository;
    private InMemoryUsuarioRepository usuarioRepository;
    private EventBus eventBus;

    private UsuarioDTO usuario;
    private CursoDTO curso;
    private InscripcionDTO inscripcion;

    @BeforeEach
    void setUp() {
        progresoRepository = new InMemoryProgresoRepository();
        cursoRepository = new InMemoryCursoRepository();
        usuarioRepository = new InMemoryUsuarioRepository();
        eventBus = new EventBus();

        cursoService = new CursoService(cursoRepository);
        usuarioService = new UsuarioService(usuarioRepository);
        progresoService = new EjecucionDeCursoService(progresoRepository, usuarioRepository, cursoRepository, eventBus);
        inscripcionService = new InscripcionService(cursoRepository, usuarioRepository);

        usuario = usuarioService.registrarUsuario("juan@example.com", "Juan");
        curso = cursoService.crearCurso("Español Básico");
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Hello?", "hola"));
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Goodbye?", "adios"));

        inscripcion = inscripcionService.inscribirUsuario(usuario.id(), curso.id());
    }

    @Test
    void iniciarProgresoParaInscripcion() {
        ProgresoDTO progreso = progresoService.iniciarCurso(usuario.id(), curso.id());

        assertNotNull(progreso.id());
    }

    @Test
    void responderPreguntaCorrectamente() {
        ProgresoDTO progreso = progresoService.iniciarCurso(usuario.id(), curso.id());

        Optional<PreguntaPresentacionDTO> r = progresoService.presentarPregunta(progreso.id());
        assertTrue(r.isPresent());
        PreguntaPresentacionDTO p = r.get();
        assertTrue(p instanceof PreguntaAbiertaPresentacionDTO);
        assertEquals(0, p.id());

        boolean isOk = progresoService.responderPregunta(progreso.id(), new RespuestaTextoDTO("hola"));
        assertTrue(isOk);

        Optional<PreguntaPresentacionDTO> r2 = progresoService.presentarPregunta(progreso.id());
        assertTrue(r2.isPresent());
        PreguntaPresentacionDTO p2 = r2.get();
        assertTrue(p2 instanceof PreguntaAbiertaPresentacionDTO);
        assertEquals(1, p2.id());


        // Ahora, pasa a la siguiente pregunta
        isOk = progresoService.responderPregunta(progreso.id(), new RespuestaTextoDTO("adios"));
        assertTrue(isOk);
    }
}
