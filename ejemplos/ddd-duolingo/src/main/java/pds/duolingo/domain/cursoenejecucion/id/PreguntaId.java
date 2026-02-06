package pds.duolingo.domain.cursoenejecucion.id;

/**
 * El id de una pregunta es local al curso. Puesto que un curso es una
 * secuencia de preguntas, podemos usar el índice.
 */
public record PreguntaId(int indice) {
    public PreguntaId {
        if (indice < 0) {
            throw new IllegalArgumentException("PreguntaId es un índice");
        }
    }

}
