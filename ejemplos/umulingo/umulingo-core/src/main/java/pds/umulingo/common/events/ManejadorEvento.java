package pds.umulingo.common.events;

@FunctionalInterface
public interface ManejadorEvento<T extends EventoDominio> {
    void manejar(T evento);
}
