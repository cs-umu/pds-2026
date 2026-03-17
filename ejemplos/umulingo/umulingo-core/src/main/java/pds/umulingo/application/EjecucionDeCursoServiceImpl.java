package pds.umulingo.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pds.umulingo.common.events.EventBus;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.model.Pregunta;
import pds.umulingo.domain.model.curso.model.PreguntaAbierta;
import pds.umulingo.domain.model.curso.model.PreguntaHuecos;
import pds.umulingo.domain.model.curso.model.PreguntaTest;
import pds.umulingo.domain.model.curso.model.Respuesta;
import pds.umulingo.domain.model.curso.model.RespuestaHuecos;
import pds.umulingo.domain.model.curso.model.RespuestaOpcion;
import pds.umulingo.domain.model.curso.model.RespuestaTexto;
import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.cursoenejecucion.model.CursoEnEjecucion;
import pds.umulingo.domain.model.cursoenejecucion.repository.CursoEnEjecucionRepository;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;
import pds.umulingo.domain.ports.input.EjecucionDeCursoService;
import pds.umulingo.domain.ports.input.dto.ParteTextoPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaAbiertaPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaHuecosPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.PreguntaTestPresentacionDTO;
import pds.umulingo.domain.ports.input.dto.ProgresoDTO;
import pds.umulingo.domain.ports.input.dto.RespuestaDTO;

@Service
public class EjecucionDeCursoServiceImpl implements EjecucionDeCursoService {
    private final CursoEnEjecucionRepository progresoRepository;
    private final CursoRepository cursoRepository;
	private final UsuarioRepository usuarioRepository;
    private final EventBus eventBus;

    public EjecucionDeCursoServiceImpl(CursoEnEjecucionRepository progresoRepository,
                           UsuarioRepository usuarioRepository,
                           CursoRepository cursoRepository,
                           EventBus eventBus) {
        this.progresoRepository = progresoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.eventBus = eventBus;
    }

    @Override
    public ProgresoDTO iniciarCurso(String usuarioId, String cursoId) {
        UsuarioId usuarioIdVO = new UsuarioId(usuarioId);
        CursoId cursoIdVO = new CursoId(cursoId);

    	Usuario usuario = usuarioRepository.findById(usuarioIdVO).orElseThrow();
    	Curso curso = Preconditions.checkNotNull(cursoRepository.findById(cursoIdVO));

    	CursoEnEjecucion progreso = progresoRepository.findByUsuarioCurso(usuario.getId(), curso.getId())
    			.orElseGet(() -> {
    				CursoEnEjecucion p = curso.iniciarCurso(usuario);
    				progresoRepository.save(p);
    				return p; 
    			});
    	    	
        return toDTO(progreso);
    }

    @Override
    public Optional<PreguntaPresentacionDTO> presentarPregunta(String progresoId) {
        CursoEnEjecucionId progresoIdVO = new CursoEnEjecucionId(progresoId);
        CursoEnEjecucion cursoEnEjecucion = progresoRepository.findById(progresoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Progreso no encontrado: " + progresoId));

        if (cursoEnEjecucion.isCompletado()) {
            return Optional.empty();
        }

        Curso curso = Preconditions.checkNotNull(cursoRepository.findById(cursoEnEjecucion.getCursoId()));

        Pregunta pregunta = cursoEnEjecucion.getPreguntaActual(curso);
        return Optional.ofNullable(pregunta).map(this::toPreguntaDTO);
    }


    public boolean responderPregunta(String progresoId, RespuestaDTO respuestaDTO) {
        CursoEnEjecucionId progresoIdVO = new CursoEnEjecucionId(progresoId);
        CursoEnEjecucion cursoEnEjecucion = progresoRepository.findById(progresoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Progreso no encontrado para el progreso: " + progresoId));

        Curso curso = Preconditions.checkNotNull(cursoRepository.findById(cursoEnEjecucion.getCursoId()));

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
            case RespuestaDTO.RespuestaOpcionDTO r -> new RespuestaOpcion(r.opcionSeleccionada());
            case RespuestaDTO.RespuestaTextoDTO r -> new RespuestaTexto(r.texto());
            case RespuestaDTO.RespuestaHuecosDTO r -> new RespuestaHuecos(r.huecosCompletados());
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
