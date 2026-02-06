package pds.duolingo.domain.organizacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pds.duolingo.domain.curso.model.Pregunta;

public class OrganizacionAleatoria implements OrganizacionCurso {
    private final Random random;

    public OrganizacionAleatoria() {
        this.random = new Random();
    }

    public OrganizacionAleatoria(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public List<Pregunta> ordenar(List<Pregunta> preguntas) {
        List<Pregunta> copia = new ArrayList<>(preguntas);
        Collections.shuffle(copia, random);
        return copia;
    }
}
