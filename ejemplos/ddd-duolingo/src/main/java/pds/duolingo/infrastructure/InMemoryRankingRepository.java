package pds.duolingo.infrastructure;

import java.util.HashMap;
import java.util.Map;

import pds.duolingo.domain.ranking.model.PosicionUsuario;
import pds.duolingo.domain.ranking.model.Ranking;
import pds.duolingo.domain.ranking.repository.RankingRepository;
import pds.duolingo.domain.usuario.id.UsuarioId;

public class InMemoryRankingRepository implements RankingRepository {

	private Map<UsuarioId, PosicionUsuario> datos = new HashMap<>();
	
	@Override
	public void save(PosicionUsuario posicion) {
		this.datos.put(posicion.getUsuarioId(), posicion);
	}
	
	public Ranking getRanking() {
		return new Ranking(this.datos.values());
	}

}
