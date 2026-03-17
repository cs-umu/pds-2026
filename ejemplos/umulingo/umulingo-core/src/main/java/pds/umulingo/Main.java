package pds.umulingo;

import java.util.List;

import pds.umulingo.application.CursoServiceImpl;
import pds.umulingo.application.EjecucionDeCursoServiceImpl;
import pds.umulingo.application.InscripcionServiceImpl;
import pds.umulingo.application.RankingEventHandler;
import pds.umulingo.application.UsuarioServiceImpl;
import pds.umulingo.common.events.EventBus;
import pds.umulingo.common.events.EventBusSimple;
import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.model.cursoenejecucion.eventos.PreguntaRespondida;
import pds.umulingo.domain.model.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;
import pds.umulingo.domain.ports.input.EjecucionDeCursoService;
import pds.umulingo.domain.ports.input.dto.CursoDTO;
import pds.umulingo.domain.ports.input.dto.ParteTextoDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaAbiertaDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaHuecosDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestDTO;
import pds.umulingo.domain.ports.input.dto.ProgresoDTO;
import pds.umulingo.domain.ports.input.dto.UsuarioDTO;
import pds.umulingo.infrastructure.InMemoryCursoRepository;
import pds.umulingo.infrastructure.InMemoryProgresoRepository;
import pds.umulingo.infrastructure.InMemoryRankingRepository;
import pds.umulingo.infrastructure.InMemoryUsuarioRepository;
import pds.umulingo.ui.ConsolaDuolingo;

public class Main {
    public static void main(String[] args) {
        // Inicializar repositorios
        UsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();
        CursoRepository cursoRepository = new InMemoryCursoRepository();
        CursoEnEjecucionRepository progresoRepository = new InMemoryProgresoRepository();
        RankingRepository rankingRepository = new InMemoryRankingRepository();

        // Inicializar event bus y ranking
        EventBusSimple eventBus = new EventBusSimple();
        RankingEventHandler rankingHandler = new RankingEventHandler(rankingRepository);
        eventBus.suscribir(PreguntaRespondida.class, rankingHandler);

        // Inicializar servicios
        UsuarioServiceImpl usuarioService = new UsuarioServiceImpl(usuarioRepository);
        CursoServiceImpl cursoService = new CursoServiceImpl(cursoRepository);
        InscripcionServiceImpl inscripcionService = new InscripcionServiceImpl(
                cursoRepository, usuarioRepository);
        EjecucionDeCursoService progresoService = new EjecucionDeCursoServiceImpl(
                progresoRepository, usuarioRepository, cursoRepository, eventBus);

        // Crear datos de prueba
        UsuarioDTO usuario = crearUsuarioPrueba(usuarioService);
        CursoDTO curso = crearCursoPrueba(cursoService);

        System.out.println("===========================================");
        System.out.println("          DUOLINGO - Bienvenido            ");
        System.out.println("===========================================");
        System.out.println();

        // Mostrar usuario disponible
        System.out.println("Usuario disponible:");
        System.out.printf("  ID: %s%n", usuario.id());
        System.out.printf("  Nombre: %s%n", usuario.nombre());
        System.out.println();

        // Mostrar curso disponible
        System.out.println("Curso disponible:");
        System.out.printf("  ID: %s%n", curso.id());
        System.out.printf("  Nombre: %s%n", curso.nombre());
        System.out.println();


        String usuarioId = usuario.id();
        String cursoId = curso.id();

        // Inscribir usuario y comenzar curso
        try {
            inscripcionService.inscribirUsuario(usuarioId, cursoId);
        } catch (Exception e) {
            System.out.println("Error al inscribir: " + e.getMessage());
            return;
        }

        ProgresoDTO progreso = progresoService.iniciarCurso(usuarioId, cursoId);
        System.out.println();
        System.out.printf("Progreso creado con ID: %s%n", progreso.id());
        System.out.println();

        // Iniciar la consola interactiva
        ConsolaDuolingo consola = new ConsolaDuolingo(progresoService);
        consola.iniciar(progreso.id());
    }

    private static UsuarioDTO crearUsuarioPrueba(UsuarioServiceImpl usuarioService) {
        return usuarioService.registrarUsuario("juan@example.com", "Juan Perez");
    }

    private static CursoDTO crearCursoPrueba(CursoServiceImpl cursoService) {
        CursoDTO curso = cursoService.crearCurso("ingles-basico", "Ingles Basico");

        // Pregunta tipo test
        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "Como se dice 'gato' en ingles?",
                List.of("Dog", "Cat", "Bird", "Fish"),
                1 // Cat es la respuesta correcta (indice 1)
        ));

        // Pregunta abierta
        cursoService.agregarPregunta(curso.id(), new PreguntaAbiertaDTO(
                "Traduce al ingles: 'Hola, como estas?'",
                "Hello, how are you?"
        ));

        // Pregunta con huecos
        cursoService.agregarPregunta(curso.id(), new PreguntaHuecosDTO(
                "Completa la oración para decir 'Soy un estudiante':",
                List.of(
                        new ParteTextoDTO("I ", false),
                        new ParteTextoDTO("am", true),
                        new ParteTextoDTO(" a ", false),
                        new ParteTextoDTO("student", true),
                        new ParteTextoDTO(".", false)
                )
        ));

        // Otra pregunta tipo test
        cursoService.agregarPregunta(curso.id(), new PreguntaTestDTO(
                "Cual es el plural de 'child'?",
                List.of("Childs", "Children", "Childes", "Child"),
                1 // Children es la respuesta correcta
        ));

        return curso;
    }
}
