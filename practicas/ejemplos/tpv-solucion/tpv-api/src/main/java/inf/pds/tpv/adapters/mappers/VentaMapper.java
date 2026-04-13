package inf.pds.tpv.adapters.mappers;

import org.springframework.stereotype.Component;

import inf.pds.tpv.adapters.memory.entity.LineaVentaMemory;
import inf.pds.tpv.adapters.memory.entity.VentaMemory;
import inf.pds.tpv.adapters.rest.dto.LineaVentaDTO;
import inf.pds.tpv.adapters.rest.dto.VentaDTO;
import inf.pds.tpv.domain.model.venta.LineaVenta;
import inf.pds.tpv.domain.model.venta.Venta;

@Component
public class VentaMapper {

	private ProductoMapper productoMapper;

	public VentaMapper(ProductoMapper productoMapper) {
		this.productoMapper = productoMapper;
	}

	public Venta toModel(VentaDTO ventaDTO) {
		Venta venta;
		if (ventaDTO.getIdentificador() == null) {
			venta = new Venta();
		} else {
			venta = new Venta(ventaDTO.getIdentificador());
		}
		for (LineaVentaDTO lineaVentaDTO : ventaDTO.getLineasVenta()) {
			venta.aniadeLineaVenta(productoMapper.toModel(lineaVentaDTO.getProducto()), lineaVentaDTO.getCantidad());
		}

		return venta;

	}

	public Venta toModel(VentaMemory ventaMemory) {

		Venta venta = new Venta(ventaMemory.getIdentificador());
		for (LineaVentaMemory lineaVentaMemory : ventaMemory.getLineasVenta()) {
			venta.aniadeLineaVenta(productoMapper.toModel(lineaVentaMemory.getProducto()),
					lineaVentaMemory.getCantidad());
		}

		return venta;

	}

	public VentaMemory toMemoryEntity(Venta venta) {

		VentaMemory ventaMemory = new VentaMemory(venta.getIdentificador());

		for (LineaVenta lineaVenta : venta.getLineasVenta()) {
			ventaMemory.getLineasVenta().add(new LineaVentaMemory(
					productoMapper.toMemoryEntity(lineaVenta.getProducto()), lineaVenta.getCantidad()));

		}

		return ventaMemory;

	}

	public VentaDTO toDTO(Venta venta) {

		VentaDTO ventaDTO = new VentaDTO(venta.getIdentificador());

		for (LineaVenta lineaVenta : venta.getLineasVenta()) {
			ventaDTO.getLineasVenta()
					.add(new LineaVentaDTO(productoMapper.toDTO(lineaVenta.getProducto()), lineaVenta.getCantidad()));

		}

		return ventaDTO;

	}

}
