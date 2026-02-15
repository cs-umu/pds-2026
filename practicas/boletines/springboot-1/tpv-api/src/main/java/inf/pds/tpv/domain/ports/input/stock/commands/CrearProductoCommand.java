package inf.pds.tpv.domain.ports.input.stock.commands;

public record CrearProductoCommand(String descripcion, int cantidad, double precio) {
}
