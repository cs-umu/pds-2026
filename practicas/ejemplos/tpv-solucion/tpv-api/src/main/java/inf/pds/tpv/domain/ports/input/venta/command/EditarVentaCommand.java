package inf.pds.tpv.domain.ports.input.venta.command;

import java.util.UUID;

import inf.pds.tpv.domain.model.venta.Venta;

public record EditarVentaCommand(UUID identificador, Venta venta) {
}
