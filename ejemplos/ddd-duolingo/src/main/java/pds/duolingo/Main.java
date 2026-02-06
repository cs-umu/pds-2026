package pds.duolingo;

import java.util.List;

import pds.duolingo.application.CursoService;
import pds.duolingo.application.InscripcionService;
import pds.duolingo.application.EjecucionDeCursoService;
import pds.duolingo.application.UsuarioService;
import pds.duolingo.application.dto.CursoDTO;
import pds.duolingo.application.dto.ParteTextoDTO;
import pds.duolingo.application.dto.PreguntaAbiertaDTO;
import pds.duolingo.application.dto.PreguntaHuecosDTO;
import pds.duolingo.application.dto.PreguntaTestDTO;
import pds.duolingo.application.dto.ProgresoDTO;
import pds.duolingo.application.dto.UsuarioDTO;
import pds.duolingo.common.events.EventBus;
import pds.duolingo.domain.curso.repository.CursoRepository;
import pds.duolingo.domain.cursoenejecucion.eventos.PreguntaRespondida;
import pds.duolingo.domain.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.duolingo.domain.ranking.RankingEventHandler;
import pds.duolingo.domain.ranking.repository.RankingRepository;
import pds.duolingo.domain.usuario.repository.UsuarioRepository;
import pds.duolingo.infrastructure.InMemoryCursoRepository;
import pds.duolingo.infrastructure.InMemoryProgresoRepository;
import pds.duolingo.infrastructure.InMemoryRankingRepository;
import pds.duolingo.infrastructure.InMemoryUsuarioRepository;
import pds.duolingo.ui.ConsolaDuolingo;

public class Main {
    public static void main(String[] args) {
        // Inicializar repositorios
        UsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();
        CursoRepository cursoRepository = new InMemoryCursoRepository();
        CursoEnEjecucionRepository progresoRepository = new InMemoryProgresoRepository();
        RankingRepository rankingRepository = new InMemoryRankingRepository();

        // Inicializar event bus y ranking
        EventBus eventBus = new EventBus();
        RankingEventHandler rankingHandler = new RankingEventHandler(rankingRepository);
        eventBus.suscribir(PreguntaRespondida.class, rankingHandler);

        // Inicializar servicios
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        CursoService cursoService = new CursoService(cursoRepository);
        InscripcionService inscripcionService = new InscripcionService(
                cursoRepository, usuarioRepository);
        EjecucionDeCursoService progresoService = new EjecucionDeCursoService(
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

    private static UsuarioDTO crearUsuarioPrueba(UsuarioService usuarioService) {
        return usuarioService.registrarUsuario("juan@example.com", "Juan Perez");
    }

    private static CursoDTO crearCursoPrueba(CursoService cursoService) {
        CursoDTO curso = cursoService.crearCurso("Ingles Basico");

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
