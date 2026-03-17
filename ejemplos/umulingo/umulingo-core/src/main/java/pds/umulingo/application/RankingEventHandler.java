package pds.umulingo.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pds.umulingo.common.events.ManejadorEvento;
import pds.umulingo.domain.model.cursoenejecucion.eventos.PreguntaRespondida;
import pds.umulingo.domain.model.ranking.model.PosicionUsuario;
import pds.umulingo.domain.model.ranking.model.Ranking;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;

@Component
public class RankingEventHandler implements ManejadorEvento<PreguntaRespondida> {
	private static final Logger log = LoggerFactory.getLogger(RankingEventHandler.class);
	
	private final Ranking ranking;
	private final RankingRepository rankingRepository;

    public RankingEventHandler(RankingRepository rankingRepository) {
        this.ranking = rankingRepository.getRanking();
    	this.rankingRepository = rankingRepository;
    }

    @EventListener
    public void preguntaRespondida(PreguntaRespondida evento) {
    	manejar(evento);
    }

	@Override
	public void manejar(PreguntaRespondida evento) {
		if (evento.correcta()) {
    		log.info("Registrada pregunta {}", evento.pregunta());
            PosicionUsuario posicion = ranking.registrarRespuestaCorrecta(evento.usuarioId());
            rankingRepository.save(posicion);
    	}
	}
    
}
