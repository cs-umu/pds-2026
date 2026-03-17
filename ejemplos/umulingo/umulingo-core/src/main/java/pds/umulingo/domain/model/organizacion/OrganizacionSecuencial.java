package pds.umulingo.domain.model.organizacion;

import java.util.List;

import pds.umulingo.domain.model.curso.model.Pregunta;

public class OrganizacionSecuencial implements OrganizacionCurso {
    @Override
    public List<Pregunta> ordenar(List<Pregunta> preguntas) {
        return List.copyOf(preguntas);
    }
}
