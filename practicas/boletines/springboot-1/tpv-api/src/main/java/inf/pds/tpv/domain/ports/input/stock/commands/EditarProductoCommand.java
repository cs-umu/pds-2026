package inf.pds.tpv.domain.ports.input.stock.commands;

public record EditarProductoCommand(Long codigo, String descripcion, int cantidad, double precio) {
}
