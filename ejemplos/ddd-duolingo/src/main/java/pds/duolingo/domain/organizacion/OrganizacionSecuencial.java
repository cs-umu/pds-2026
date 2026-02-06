package pds.duolingo.domain.organizacion;

import java.util.List;

import pds.duolingo.domain.curso.model.Pregunta;

public class OrganizacionSecuencial implements OrganizacionCurso {
    @Override
    public List<Pregunta> ordenar(List<Pregunta> preguntas) {
        return List.copyOf(preguntas);
    }
}
