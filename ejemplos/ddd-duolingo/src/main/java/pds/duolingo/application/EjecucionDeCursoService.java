package pds.duolingo.application;

import java.util.Optional;

import pds.duolingo.application.dto.ParteTextoPresentacionDTO;
import pds.duolingo.application.dto.PreguntaAbiertaPresentacionDTO;
import pds.duolingo.application.dto.PreguntaHuecosPresentacionDTO;
import pds.duolingo.application.dto.PreguntaPresentacionDTO;
import pds.duolingo.application.dto.PreguntaTestPresentacionDTO;
import pds.duolingo.application.dto.ProgresoDTO;
import pds.duolingo.application.dto.RespuestaDTO;
import pds.duolingo.application.dto.RespuestaHuecosDTO;
import pds.duolingo.application.dto.RespuestaOpcionDTO;
import pds.duolingo.application.dto.RespuestaTextoDTO;
import pds.duolingo.common.events.EventBus;
import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.model.Curso;
import pds.duolingo.domain.curso.model.Pregunta;
import pds.duolingo.domain.curso.model.PreguntaAbierta;
import pds.duolingo.domain.curso.model.PreguntaHuecos;
import pds.duolingo.domain.curso.model.PreguntaTest;
import pds.duolingo.domain.curso.model.Respuesta;
import pds.duolingo.domain.curso.model.RespuestaHuecos;
import pds.duolingo.domain.curso.model.RespuestaOpcion;
import pds.duolingo.domain.curso.model.RespuestaTexto;
import pds.duolingo.domain.curso.repository.CursoRepository;
import pds.duolingo.domain.cursoenejecucion.id.CursoEnEjecucionId;
import pds.duolingo.domain.cursoenejecucion.model.CursoEnEjecucion;
import pds.duolingo.domain.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.duolingo.domain.usuario.id.UsuarioId;
import pds.duolingo.domain.usuario.model.Usuario;
import pds.duolingo.domain.usuario.repository.UsuarioRepository;

public class EjecucionDeCursoService {
    private final CursoEnEjecucionRepository progresoRepository;
    private final CursoRepository cursoRepository;
	private final UsuarioRepository usuarioRepository;
    private final EventBus eventBus;

    public EjecucionDeCursoService(CursoEnEjecucionRepository progresoRepository,
                           UsuarioRepository usuarioRepository,
                           CursoRepository cursoRepository,
                           EventBus eventBus) {
        this.progresoRepository = progresoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.eventBus = eventBus;
    }

    public ProgresoDTO iniciarCurso(String usuarioId, String cursoId) {
        UsuarioId usuarioIdVO = new UsuarioId(usuarioId);
        CursoId cursoIdVO = new CursoId(cursoId);

    	Usuario usuario = usuarioRepository.findById(usuarioIdVO).orElseThrow();
    	Curso curso = cursoRepository.findById(cursoIdVO).orElseThrow();

    	CursoEnEjecucion progreso = curso.iniciarCurso(usuario);
        progresoRepository.save(progreso);
        return toDTO(progreso);
    }

    public Optional<PreguntaPresentacionDTO> presentarPregunta(String progresoId) {
        CursoEnEjecucionId progresoIdVO = new CursoEnEjecucionId(progresoId);
        CursoEnEjecucion cursoEnEjecucion = progresoRepository.findById(progresoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Progreso no encontrado: " + progresoId));

        if (cursoEnEjecucion.isCompletado()) {
            return Optional.empty();
        }

        Curso curso = cursoRepository.findById(cursoEnEjecucion.getCursoId())
                .orElseThrow(() -> new IllegalStateException("Curso no encontrado: " + cursoEnEjecucion.getCursoId()));

        Pregunta pregunta = cursoEnEjecucion.getPreguntaActual(curso);
        return Optional.ofNullable(pregunta).map(this::toPreguntaDTO);
    }


    public boolean responderPregunta(String progresoId, RespuestaDTO respuestaDTO) {
        CursoEnEjecucionId progresoIdVO = new CursoEnEjecucionId(progresoId);
        CursoEnEjecucion cursoEnEjecucion = progresoRepository.findById(progresoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Progreso no encontrado para el progreso: " + progresoId));

        Curso curso = cursoRepository.findById(cursoEnEjecucion.getCursoId())
                .orElseThrow(() -> new IllegalStateException("Curso no encontrado: " + cursoEnEjecucion.getCursoId()));

        Respuesta respuesta = toRespuesta(respuestaDTO);
        boolean esCorrecta = cursoEnEjecucion.registrarRespuesta(curso, respuesta);
        progresoRepository.save(cursoEnEjecucion);
        eventBus.publicarTodos(cursoEnEjecucion);

        return esCorrecta;
    }

    private ProgresoDTO toDTO(CursoEnEjecucion progreso) {
        return new ProgresoDTO(
            progreso.getId().valor(),
            progreso.getUsuarioId().valor(),
            progreso.getCursoId().valor()
        );
    }

    private PreguntaPresentacionDTO toPreguntaDTO(Pregunta pregunta) {
        return switch (pregunta) {
            case PreguntaTest pt -> new PreguntaTestPresentacionDTO(
                pt.getId().indice(),
                pt.getEnunciado(),
                pt.getOpciones()
            );
            case PreguntaAbierta pa -> new PreguntaAbiertaPresentacionDTO(
                pa.getId().indice(),
                pa.getEnunciado()
            );
            case PreguntaHuecos ph -> new PreguntaHuecosPresentacionDTO(
                ph.getId().indice(),
                ph.getEnunciado(),
                ph.getPartes().stream()
                    .map(p -> new ParteTextoPresentacionDTO(p.esHueco() ? "" : p.getTexto(), p.esHueco()))
                    .toList()
            );
            default -> throw new IllegalArgumentException("Tipo de pregunta no soportado: " + pregunta.getClass());
        };
    }

    private Respuesta toRespuesta(RespuestaDTO dto) {
        return switch (dto) {
            case RespuestaOpcionDTO r -> new RespuestaOpcion(r.opcionSeleccionada());
            case RespuestaTextoDTO r -> new RespuestaTexto(r.texto());
            case RespuestaHuecosDTO r -> new RespuestaHuecos(r.huecosCompletados());
        };
    }

    public static record EstadoProgreso(
            int preguntaActual,
            int totalPreguntas,
            int respuestasCorrectas,
            int respuestasIncorrectas,
            boolean completado
    ) {
        public double getPorcentajeProgreso() {
            if (totalPreguntas == 0) return 0;
            return (double) preguntaActual / totalPreguntas * 100;
        }

        public double getPorcentajeAciertos() {
            int total = respuestasCorrectas + respuestasIncorrectas;
            if (total == 0) return 0;
            return (double) respuestasCorrectas / total * 100;
        }
    }

}
