package pds.duolingo.domain.organizacion;

import java.util.List;

import pds.duolingo.domain.curso.model.Pregunta;

public interface OrganizacionCurso {
    List<Pregunta> ordenar(List<Pregunta> preguntas);
}
