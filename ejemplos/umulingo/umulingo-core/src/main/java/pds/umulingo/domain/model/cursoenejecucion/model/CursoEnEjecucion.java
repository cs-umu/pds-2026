package pds.umulingo.domain.model.cursoenejecucion.model;

import pds.umulingo.common.events.AgregadoConEventos;
import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.model.Pregunta;
import pds.umulingo.domain.model.curso.model.Respuesta;
import pds.umulingo.domain.model.cursoenejecucion.eventos.PreguntaRespondida;
import pds.umulingo.domain.model.cursoenejecucion.id.CursoEnEjecucionId;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

public class CursoEnEjecucion extends AgregadoConEventos.Impl {
	private final CursoEnEjecucionId id;
    private final UsuarioId usuarioId;
    private final CursoId cursoId;
    
    private int preguntaActual;

    // Estadisticas
    private int respuestasCorrectas;
    private int respuestasIncorrectas;
    private boolean completado;

    private CursoEnEjecucion(CursoEnEjecucionId id, CursoId cursoId, UsuarioId usuarioId, int preguntaActual) {
    	this.id = id;
    	this.usuarioId = usuarioId;
    	this.cursoId = cursoId;
    	this.preguntaActual = preguntaActual;
    }
    
    public CursoEnEjecucion(UsuarioId usuarioId, CursoId cursoId) {
        this.id = CursoEnEjecucionId.nuevo();
        this.usuarioId = usuarioId;
        this.cursoId = cursoId;
        
        this.preguntaActual = 0;
        this.respuestasCorrectas = 0;
        this.respuestasIncorrectas = 0;
        this.completado = false;
    }

    public CursoEnEjecucionId getId() {
        return id;
    }

	public CursoId getCursoId() {
		return cursoId;
	}

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }
	
    public int getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public int getRespuestasIncorrectas() {
        return respuestasIncorrectas;
    }

    public boolean isCompletado() {
        return completado;
    }
    
    public Pregunta getPreguntaActual(Curso curso) {
    	return curso.getPregunta(this.preguntaActual);
	}

    // Esto podría estar un servicio de dominio
    public boolean registrarRespuesta(Curso c, Respuesta r) {
    	if (completado) {
            throw new IllegalStateException("El curso ya está completado");
        }
    	
    	Pregunta p = c.getPregunta(this.preguntaActual);
    	boolean correcta = c.verificarRespuesta(p.getId(), r);
    	if (correcta) {
    		respuestasCorrectas++;
    	} else {
    		respuestasIncorrectas++;
    	}
    	registrarEvento(new PreguntaRespondida(usuarioId, p.getId(), correcta));
        preguntaActual++;
        if (preguntaActual >= c.getTotalPreguntas()) {
            completado = true;
        }
        return correcta;
    }
    
    public double getPorcentajeProgreso(int totalPreguntas) {
        if (totalPreguntas == 0) return 0;
        return (double) preguntaActual / totalPreguntas * 100;
    }

    public double getPorcentajeAciertos() {
        int totalRespondidas = respuestasCorrectas + respuestasIncorrectas;
        if (totalRespondidas == 0) return 0;
        return (double) respuestasCorrectas / totalRespondidas * 100;
    }

    /**
     * Estos métodos solo están disponibles para la persistencia.
     */
	public int getEstado() {
		return preguntaActual;
	}
	
	// Alternativa
	public <T> T toEntity(EntityFactory factory) {
		return (T) factory.toEntity(id, usuarioId, cursoId, preguntaActual);
	}

	@FunctionalInterface
	public static interface EntityFactory<T> {
		public T toEntity(CursoEnEjecucionId id, UsuarioId usuarioId, CursoId cursoId, int preguntaActual);
	}

	public static CursoEnEjecucion fromEntity(CursoEnEjecucionId id, CursoId cursoId, UsuarioId usuarioId, int estadoProgreso) {
		return new CursoEnEjecucion(id, cursoId, usuarioId, estadoProgreso);
	}

}
