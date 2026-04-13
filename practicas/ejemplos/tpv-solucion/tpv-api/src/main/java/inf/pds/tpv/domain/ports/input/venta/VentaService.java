package inf.pds.tpv.domain.ports.input.venta;

import java.util.List;
import java.util.UUID;

import inf.pds.tpv.domain.model.venta.Venta;
import inf.pds.tpv.domain.ports.input.venta.command.CrearVentaCommand;
import inf.pds.tpv.domain.ports.input.venta.command.EditarVentaCommand;
import inf.pds.tpv.domain.ports.input.venta.command.EliminarVentaCommand;

public interface VentaService {

	public Venta crearVenta(CrearVentaCommand crearVenta);
	
	public Venta modificarVenta(EditarVentaCommand editarVenta);
	
	public List<Venta> obtenerTodasVentas();
	
	public Venta obtenerVenta(UUID idVenta);
	
	public void eliminarVenta(EliminarVentaCommand eliminarVenta);
	
}
