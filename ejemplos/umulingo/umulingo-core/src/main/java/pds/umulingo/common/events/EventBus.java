package pds.umulingo.common.events;

public interface EventBus {
    public void publicar(EventoDominio evento);

    public void publicarTodos(AgregadoConEventos agregado);
}
