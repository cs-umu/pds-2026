package pds.duolingo.common.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private final Map<Class<? extends EventoDominio>, List<ManejadorEvento<?>>> manejadores = new HashMap<>();

    public <T extends EventoDominio> void suscribir(Class<T> tipoEvento, ManejadorEvento<T> manejador) {
        manejadores.computeIfAbsent(tipoEvento, k -> new ArrayList<>()).add(manejador);
    }

    @SuppressWarnings("unchecked")
    public void publicar(EventoDominio evento) {
        List<ManejadorEvento<?>> lista = manejadores.get(evento.getClass());
        if (lista != null) {
            for (ManejadorEvento<?> manejador : lista) {
                ((ManejadorEvento<EventoDominio>) manejador).manejar(evento);
            }
        }
    }

    // Esto no es totalmente correcto porque haría que se ejecutaran en la misma transacción
    // En la práctica hay que hacerlo en dos pasos: publicar y luego el event bus tiene que 
    // recoger los más recientes y enviarlos a los manejadores
    public void publicarTodos(AgregadoConEventos agregado) {
    	List<EventoDominio> eventos = agregado.extraerEventos();
        for (EventoDominio evento : eventos) {
            publicar(evento);
        }
    }
}
