package pds.duolingo.common.events;

import java.util.ArrayList;
import java.util.List;

public interface AgregadoConEventos {
	List<EventoDominio> extraerEventos();
    
	public static abstract class Impl implements AgregadoConEventos {
		private final List<EventoDominio> eventos = new ArrayList<>();
	
		protected void registrarEvento(EventoDominio evento) {
	        eventos.add(evento);
	    }
	
	    @Override
	    public List<EventoDominio> extraerEventos() {
	        List<EventoDominio> copia = List.copyOf(eventos);
	        eventos.clear();
	        return copia;
	    }
	}

}
