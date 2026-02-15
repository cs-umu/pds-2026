package inf.pds.tpv.adapters.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import inf.pds.tpv.adapters.jpa.entity.ProductoEntity;
import inf.pds.tpv.adapters.jpa.repository.ProductoJpaRepository;
import inf.pds.tpv.adapters.mappers.ProductoMapper;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.ports.output.ProductosRepository;

//Anotacion de Springboot para que carge esta clase como EJB y pueda ser inyectada en cualquier otra
@Component
public class ProductoRepository implements ProductosRepository {

	private ProductoJpaRepository productoJpaRepository;
	private ProductoMapper productoMapper;

	public ProductoRepository(ProductoJpaRepository productoRepository, ProductoMapper productoMapper) {
		this.productoJpaRepository = productoRepository;
		this.productoMapper = productoMapper;
	}

	@Override
	public List<Producto> obtenerTodosProductos() {
		List<ProductoEntity> listaProductos = this.productoJpaRepository.findAll();
		return listaProductos.stream().map(productoMapper::toModel).toList();
	}

	@Override
	public Producto crearNuevoProducto(Producto producto) {
		ProductoEntity productoAPersistir = new ProductoEntity(null, producto.getDescripcion(), producto.getCantidad(),
				producto.getPrecio());
		// En este momento JPA le asigna un ID haciendo uso de las anotaciones de la
		// entidad
		productoAPersistir = this.productoJpaRepository.save(productoAPersistir);
		return productoMapper.toModel(productoAPersistir);
	}

	@Override
	public Optional<Producto> obtenerProductoPorId(Long id) {
		Optional<ProductoEntity> productoEntity = this.productoJpaRepository.findById(id);
		if (productoEntity.isPresent()) {
			return Optional.of(productoMapper.toModel(productoEntity.get()));
		}
		return Optional.empty();
	}

	@Override
	public Producto editarProducto(Producto producto) {
		ProductoEntity productoGuardado = this.productoJpaRepository.save(productoMapper.toEntity(producto));
		return productoMapper.toModel(productoGuardado);
	}

	public void eliminaProducto(Producto producto) {
		this.productoJpaRepository.delete(productoMapper.toEntity(producto));
	}

}
