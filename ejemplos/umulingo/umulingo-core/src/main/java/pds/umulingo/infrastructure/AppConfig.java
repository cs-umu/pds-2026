package pds.umulingo.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pds.umulingo.application.CursoServiceImpl;
import pds.umulingo.application.InscripcionServiceImpl;
import pds.umulingo.application.RankingEventHandler;
import pds.umulingo.application.UsuarioServiceImpl;
import pds.umulingo.common.events.EventBus;
import pds.umulingo.common.events.EventBusSimple;
import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.model.cursoenejecucion.eventos.PreguntaRespondida;
import pds.umulingo.domain.model.ranking.repository.RankingRepository;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;

@Configuration
public class AppConfig {

    @Bean
    public CursoRepository cursoRepository() {
        return new InMemoryCursoRepository();
    }

    /*
    @Bean
    public UsuarioRepository usuarioRepository() {
        return new InMemoryUsuarioRepository();
    }

    @Bean
    public RankingRepository rankingRepository() {
        return new InMemoryRankingRepository();
    }

    @Bean
    public EventBus eventBus(RankingRepository rankingRepository) {
    	EventBusSimple bus = new EventBusSimple();
        bus.suscribir(PreguntaRespondida.class, new RankingEventHandler(rankingRepository));
        return bus;
    }

    @Bean
    public UsuarioServiceImpl usuarioService(UsuarioRepository usuarioRepository) {
        return new UsuarioServiceImpl(usuarioRepository);
    }

    @Bean
    public InscripcionServiceImpl inscripcionService(CursoRepository cursoRepository,
                                                 UsuarioRepository usuarioRepository) {
        return new InscripcionServiceImpl(cursoRepository, usuarioRepository);
    }

    @Bean
    public CursoServiceImpl cursoService(CursoRepository cursoRepository) {
        return new CursoServiceImpl(cursoRepository);
    }
    */

}
