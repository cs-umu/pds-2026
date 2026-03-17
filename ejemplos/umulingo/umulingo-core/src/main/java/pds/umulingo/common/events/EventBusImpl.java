package pds.umulingo.common.events;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventBusImpl implements EventBus {
    private final ApplicationEventPublisher eventPublisher;

    public EventBusImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

	@Override
	public void publicar(EventoDominio evento) {
	    eventPublisher.publishEvent(evento);
	}

	@Override
	public void publicarTodos(AgregadoConEventos agregado) {
		agregado.extraerEventos().forEach(this::publicar);
	}
	
	public class SystemEvent extends ApplicationEvent {
		private static final long serialVersionUID = 7782813960131020449L;
		private final EventoDominio evento;

	    public SystemEvent(Object source, EventoDominio evento) {
	        super(source);
	        this.evento = evento;
	    }

	    public EventoDominio getEvento() {
			return evento;
		}
	}
}