package pds.duolingo.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pds.duolingo.application.dto.CursoDTO;
import pds.duolingo.application.dto.PreguntaAbiertaDTO;
import pds.duolingo.application.dto.ProgresoDTO;
import pds.duolingo.application.dto.RespuestaTextoDTO;
import pds.duolingo.application.dto.UsuarioDTO;
import pds.duolingo.common.events.EventBus;
import pds.duolingo.domain.cursoenejecucion.eventos.PreguntaRespondida;
import pds.duolingo.domain.ranking.RankingEventHandler;
import pds.duolingo.domain.ranking.model.PosicionUsuario;
import pds.duolingo.domain.usuario.id.UsuarioId;
import pds.duolingo.infrastructure.InMemoryCursoRepository;
import pds.duolingo.infrastructure.InMemoryProgresoRepository;
import pds.duolingo.infrastructure.InMemoryRankingRepository;
import pds.duolingo.infrastructure.InMemoryUsuarioRepository;

class RankingEventoTest {

    private EjecucionDeCursoService progresoService;
    private InscripcionService inscripcionService;
    private CursoService cursoService;
    private UsuarioService usuarioService;

    private InMemoryProgresoRepository progresoRepository;
    private InMemoryCursoRepository cursoRepository;
    private InMemoryUsuarioRepository usuarioRepository;
    private InMemoryRankingRepository rankingRepository;
    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        progresoRepository = new InMemoryProgresoRepository();
        cursoRepository = new InMemoryCursoRepository();
        usuarioRepository = new InMemoryUsuarioRepository();
        rankingRepository = new InMemoryRankingRepository();
        
        eventBus = new EventBus();

        RankingEventHandler handler = new RankingEventHandler(rankingRepository);
        eventBus.suscribir(PreguntaRespondida.class, handler);

        cursoService = new CursoService(cursoRepository);
        usuarioService = new UsuarioService(usuarioRepository);
        progresoService = new EjecucionDeCursoService(progresoRepository, usuarioRepository, cursoRepository, eventBus);
        inscripcionService = new InscripcionService(cursoRepository, usuarioRepository);
    }

    @Test
    void responderPreguntaCorrectaActualizaRanking() {
        UsuarioDTO usuario = usuarioService.registrarUsuario("ana@example.com", "Ana");
        CursoDTO curso = cursoService.crearCurso("Test Curso");
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Hello?", "hola"));
        inscripcionService.inscribirUsuario(usuario.id(), curso.id());

        ProgresoDTO progreso = progresoService.iniciarCurso(usuario.id(), curso.id());
        progresoService.responderPregunta(progreso.id(), new RespuestaTextoDTO("hola"));

        Optional<PosicionUsuario> posicion = rankingRepository.getRanking().obtenerPosicion(new UsuarioId(usuario.id()));
        assertTrue(posicion.isPresent());
        assertEquals(1, posicion.get().getRespuestasCorrectas());
    }

    @Test
    void responderPreguntaIncorrectaNoActualizaRanking() {
        UsuarioDTO usuario = usuarioService.registrarUsuario("bob@example.com", "Bob");
        CursoDTO curso = cursoService.crearCurso("Test Curso");
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Hello?", "hola"));
        inscripcionService.inscribirUsuario(usuario.id(), curso.id());

        ProgresoDTO progreso = progresoService.iniciarCurso(usuario.id(), curso.id());
        progresoService.responderPregunta(progreso.id(), new RespuestaTextoDTO("incorrecto"));

        Optional<PosicionUsuario> posicion = rankingRepository.getRanking().obtenerPosicion(new UsuarioId(usuario.id()));
        assertTrue(posicion.isEmpty());
    }

    @Test
    void dosUsuariosCompitendoEnRanking() {
        UsuarioDTO ana = usuarioService.registrarUsuario("ana@example.com", "Ana");
        UsuarioDTO bob = usuarioService.registrarUsuario("bob@example.com", "Bob");

        CursoDTO curso = cursoService.crearCurso("Test Curso");
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Hello?", "hola"));
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Goodbye?", "adios"));
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO("¿Thanks?", "gracias"));

        inscripcionService.inscribirUsuario(ana.id(), curso.id());
        inscripcionService.inscribirUsuario(bob.id(), curso.id());

        // Ana responde 2 correctas
        ProgresoDTO progresoAna = progresoService.iniciarCurso(ana.id(), curso.id());
        progresoService.responderPregunta(progresoAna.id(), new RespuestaTextoDTO("hola"));
        progresoService.responderPregunta(progresoAna.id(), new RespuestaTextoDTO("adios"));

        // Bob responde 1 correcta
        ProgresoDTO progresoBob = progresoService.iniciarCurso(bob.id(), curso.id());
        progresoService.responderPregunta(progresoBob.id(), new RespuestaTextoDTO("hola"));

        List<PosicionUsuario> posiciones = rankingRepository.getRanking().obtenerPosiciones();
        assertEquals(2, posiciones.size());
        assertEquals(new UsuarioId(ana.id()), posiciones.get(0).getUsuarioId());
        assertEquals(2, posiciones.get(0).getRespuestasCorrectas());
        assertEquals(new UsuarioId(bob.id()), posiciones.get(1).getUsuarioId());
        assertEquals(1, posiciones.get(1).getRespuestasCorrectas());
    }
}
