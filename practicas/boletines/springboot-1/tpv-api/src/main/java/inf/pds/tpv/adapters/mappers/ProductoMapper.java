package inf.pds.tpv.adapters.mappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import inf.pds.tpv.adapters.jpa.entity.ProductoEntity;
import inf.pds.tpv.adapters.rest.dto.ProductoDTO;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;

@Component
public class ProductoMapper {

	private static final Logger log = LoggerFactory.getLogger(ProductoMapper.class);

	public Producto toModel(ProductoEntity productoEntity) {
		Producto producto = null;
		try {
			producto = new Producto(ProductoId.of(productoEntity.getCodigo()),
					productoEntity.getDescripcion(), productoEntity.getCantidad(), productoEntity.getPrecio());
		} catch (IdentificadorProductoException ex) {
			log.error("Error generando identificador de producto", ex);
		}
		return producto;
	}

	public ProductoEntity toEntity(Producto producto) {
		return new ProductoEntity(producto.getIdentificador().getCodigoNumerico(), producto.getDescripcion(),
				producto.getCantidad(), producto.getPrecio());
	}

	public ProductoDTO toDTO(Producto producto) {
		return new ProductoDTO(producto.getIdentificador().getCodigoNumerico(), producto.getDescripcion(), producto.getCantidad(),
				producto.getPrecio());

	}

}
