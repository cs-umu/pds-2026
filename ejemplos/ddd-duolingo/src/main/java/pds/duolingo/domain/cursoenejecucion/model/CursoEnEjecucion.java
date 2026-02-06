package pds.duolingo.domain.cursoenejecucion.model;

import pds.duolingo.common.events.AgregadoConEventos;
import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.model.Curso;
import pds.duolingo.domain.curso.model.Pregunta;
import pds.duolingo.domain.curso.model.Respuesta;
import pds.duolingo.domain.cursoenejecucion.eventos.PreguntaRespondida;
import pds.duolingo.domain.cursoenejecucion.id.CursoEnEjecucionId;
import pds.duolingo.domain.usuario.id.UsuarioId;

public class CursoEnEjecucion extends AgregadoConEventos.Impl {
	private final CursoEnEjecucionId id;
    private final UsuarioId usuarioId;
    private final CursoId cursoId;
    
    private int preguntaActual;

    // Estadisticas
    private int respuestasCorrectas;
    private int respuestasIncorrectas;
    private boolean completado;


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


}
