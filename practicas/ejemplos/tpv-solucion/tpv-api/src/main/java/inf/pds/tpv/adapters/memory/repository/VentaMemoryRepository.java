package inf.pds.tpv.adapters.memory.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import inf.pds.tpv.adapters.mappers.VentaMapper;
import inf.pds.tpv.adapters.memory.entity.VentaMemory;
import inf.pds.tpv.domain.model.venta.Venta;
import inf.pds.tpv.domain.ports.output.VentasRepository;

@Component
public class VentaMemoryRepository implements VentasRepository {

	private HashMap<UUID, VentaMemory> repository;
	private VentaMapper ventaMapper;

	public VentaMemoryRepository(VentaMapper ventaMapper) {
		repository = new HashMap<>();
		this.ventaMapper = ventaMapper;
	}

	@Override
	public Venta crearVenta(Venta venta) {
		this.repository.put(venta.getIdentificador(), ventaMapper.toMemoryEntity(venta));
		return venta;
	}

	@Override
	public Venta modificarVenta(Venta venta) {
		// Hacemos edicion por sobrescritura
		this.repository.put(venta.getIdentificador(), ventaMapper.toMemoryEntity(venta));
		return venta;
	}

	@Override
	public List<Venta> obtenerTodasVentas() {
		return this.repository.values().stream().map(ventaMemory -> ventaMapper.toModel(ventaMemory)).toList();
	}

	@Override
	public Optional<Venta> obtenerVenta(UUID idVenta) {
		VentaMemory venta = this.repository.get(idVenta);
		if (venta != null) {
			return Optional.of(ventaMapper.toModel(venta));
		}else {
			return Optional.empty();
		}
	}

	@Override
	public void eliminarVenta(UUID idVenta) {
		this.repository.remove(idVenta);
	}

}
