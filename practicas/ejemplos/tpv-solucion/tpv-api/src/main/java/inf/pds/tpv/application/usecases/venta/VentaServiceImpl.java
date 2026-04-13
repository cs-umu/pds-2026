package inf.pds.tpv.application.usecases.venta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import inf.pds.tpv.adapters.memory.repository.ProductoMemoryRepository;
import inf.pds.tpv.domain.model.venta.Venta;
import inf.pds.tpv.domain.ports.input.venta.VentaService;
import inf.pds.tpv.domain.ports.input.venta.command.CrearVentaCommand;
import inf.pds.tpv.domain.ports.input.venta.command.EditarVentaCommand;
import inf.pds.tpv.domain.ports.input.venta.command.EliminarVentaCommand;
import inf.pds.tpv.domain.ports.output.VentasRepository;

@Service
public class VentaServiceImpl implements VentaService {

	private static final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

	private VentasRepository ventasRepository;

	VentaServiceImpl(VentasRepository ventasRepository) {
		this.ventasRepository = ventasRepository;
	}

	@Override
	public Venta crearVenta(CrearVentaCommand ventaCommand) {
		return this.ventasRepository.crearVenta(ventaCommand.venta());

	}

	@Override
	public Venta modificarVenta(EditarVentaCommand venta) {
		return this.ventasRepository.modificarVenta(venta.venta());
	}

	@Override
	public List<Venta> obtenerTodasVentas() {
		return ventasRepository.obtenerTodasVentas();
	}

	@Override
	public Venta obtenerVenta(UUID idVenta) {
		Optional<Venta> venta = ventasRepository.obtenerVenta(idVenta);
		if (venta.isPresent()) {
			return venta.get();
		} else {
			return null;
		}
	}

	@Override
	public void eliminarVenta(EliminarVentaCommand eliminarVenta) {
		this.ventasRepository.eliminarVenta(eliminarVenta.identificador());
	}

}
