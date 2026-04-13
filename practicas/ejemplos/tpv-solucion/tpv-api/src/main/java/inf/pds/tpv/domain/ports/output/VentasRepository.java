package inf.pds.tpv.domain.ports.output;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import inf.pds.tpv.domain.model.venta.Venta;

public interface VentasRepository {

	public Venta crearVenta(Venta venta);

	public Venta modificarVenta(Venta venta);

	public List<Venta> obtenerTodasVentas();

	public Optional<Venta> obtenerVenta(UUID idVenta);
	
	public void eliminarVenta(UUID idVenta);

}
