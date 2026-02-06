package pds.duolingo.domain.ranking;

import pds.duolingo.common.events.ManejadorEvento;
import pds.duolingo.domain.cursoenejecucion.eventos.PreguntaRespondida;
import pds.duolingo.domain.ranking.model.PosicionUsuario;
import pds.duolingo.domain.ranking.model.Ranking;
import pds.duolingo.domain.ranking.repository.RankingRepository;

public class RankingEventHandler implements ManejadorEvento<PreguntaRespondida> {
	private final Ranking ranking;
	private final RankingRepository rankingRepository;

    public RankingEventHandler(RankingRepository rankingRepository) {
        this.ranking = rankingRepository.getRanking();
    	this.rankingRepository = rankingRepository;
    }

    @Override
    public void manejar(PreguntaRespondida evento) {
        if (evento.correcta()) {
            PosicionUsuario posicion = ranking.registrarRespuestaCorrecta(evento.usuarioId());
            rankingRepository.save(posicion);
        }
    }
}
