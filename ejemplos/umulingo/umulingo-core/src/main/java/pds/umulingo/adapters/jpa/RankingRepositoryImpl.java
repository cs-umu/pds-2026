package pds.umulingo.adapters.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import pds.umulingo.adapters.jpa.entity.PosicionUsuarioEntity;
import pds.umulingo.adapters.jpa.repository.PosicionRepositoryJPA;
import pds.umulingo.domain.model.ranking.model.PosicionUsuario;
import pds.umulingo.domain.model.ranking.model.Ranking;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;

@Component
public class RankingRepositoryImpl implements RankingRepository {

	private PosicionRepositoryJPA jpa;

	public RankingRepositoryImpl(PosicionRepositoryJPA jpa) {
		this.jpa = jpa;
	}

	@Override
	public void save(PosicionUsuario posicion) {
		PosicionUsuarioEntity entidad = new PosicionUsuarioEntity(posicion.getUsuarioId(), posicion.getRespuestasCorrectas());
		jpa.save(entidad);
	}

	@Override
	public Ranking getRanking() {
		List<PosicionUsuarioEntity> todosPuntos = jpa.findAllOrdered();
		List<PosicionUsuario> posiciones = todosPuntos.stream().map(p -> new PosicionUsuario(p.getUsuario(), p.getPuntos())).toList();
		return new Ranking(posiciones);
	}
	
}
