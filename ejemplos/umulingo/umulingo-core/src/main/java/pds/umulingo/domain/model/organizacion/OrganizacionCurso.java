package pds.umulingo.domain.model.organizacion;

import java.util.List;

import pds.umulingo.domain.model.curso.model.Pregunta;

public interface OrganizacionCurso {
    List<Pregunta> ordenar(List<Pregunta> preguntas);
}
