package pds.umulingo.infrastructure;

import java.util.HashMap;
import java.util.Map;

import pds.umulingo.domain.model.ranking.model.PosicionUsuario;
import pds.umulingo.domain.model.ranking.model.Ranking;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

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
