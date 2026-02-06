package pds.duolingo.domain.curso.model;

import java.util.ArrayList;
import java.util.List;

import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.cursoenejecucion.id.PreguntaId;
import pds.duolingo.domain.cursoenejecucion.model.CursoEnEjecucion;
import pds.duolingo.domain.usuario.model.Usuario;

public class Curso {
    private final CursoId id;
    private final String nombre;
    private final List<Pregunta> preguntas;

    public Curso(CursoId id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
    }

    public CursoId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public PreguntaId nuevaPreguntaAbierta(String enunciado, String respuestaCorrecta) {
        PreguntaId id = new PreguntaId(preguntas.size());
        preguntas.add(new PreguntaAbierta(id, enunciado, respuestaCorrecta));
        return id;
    }

    public PreguntaId nuevaPreguntaTest(String enunciado, List<String> opciones, int opcionCorrecta) {
        PreguntaId id = new PreguntaId(preguntas.size());
        preguntas.add(new PreguntaTest(id, enunciado, opciones, opcionCorrecta));
        return id;
    }

    public PreguntaId nuevaPreguntaHuecos(String enunciado, List<PreguntaHuecos.ParteTexto> partes) {
        PreguntaId id = new PreguntaId(preguntas.size());
        preguntas.add(new PreguntaHuecos(id, enunciado, partes));
        return id;
    }

    public int getTotalPreguntas() {
        return preguntas.size();
    }
    
    /**
     * El curso encapsula en esta operación todo lo que tiene que ver con identificar
     * donde está la pregunta (podría estar un bloque) y el cómo responder.
     * 
     * Alternativamente, se le podría pasar una estrategia si hay diferentes tipos
     * de formas de responder.
     */
    public boolean verificarRespuesta(PreguntaId pId, Respuesta r) {
    	Pregunta pregunta = preguntas.stream().filter(p -> p.getId() == pId).findFirst().orElseThrow();
    	return pregunta.verificarRespuesta(r);
    }

	public CursoEnEjecucion iniciarCurso(Usuario usuario) {
		// TODO: El usuario tiene que estar inscrito
		return new CursoEnEjecucion(usuario.getId(), this.id);
	}

    public Pregunta getPregunta(int indice) {
        if (indice < 0 || indice >= preguntas.size()) {
            return null;
        }
        return preguntas.get(indice);
    }
}
