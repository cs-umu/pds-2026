package pds.duolingo.domain.ranking.repository;

import pds.duolingo.domain.ranking.model.PosicionUsuario;
import pds.duolingo.domain.ranking.model.Ranking;

/**
 * El ranking tiene una interfaz algo diferente puesto solo hay un ranking global
 * y para ser eficientes lo que se desea es almacenar posiciones de manera individual.
 */
public interface RankingRepository {
    void save(PosicionUsuario posicion);

    // Obtiene la única instancia del ranking
	Ranking getRanking();
}
