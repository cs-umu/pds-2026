package inf.pds.tpv.adapters.memory.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import inf.pds.tpv.adapters.mappers.ProductoMapper;
import inf.pds.tpv.adapters.memory.entity.ProductoMemory;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.ports.output.ProductosRepository;


@Component
@Profile("memory")
public class ProductoMemoryRepository implements ProductosRepository{
	//Ejercicio 2.2 boletin: https://github.com/cs-umu/pds-2026/blob/main/practicas/boletines/springboot-1/README.md
	private ProductoMapper productoMapper;
	private HashMap<Long,ProductoMemory> productos;
	
	public ProductoMemoryRepository(ProductoMapper productoMapper) {
		productos = new HashMap<>();
		this.productoMapper = productoMapper;
	}

	@Override
	public List<Producto> obtenerTodosProductos() {		
		return productos.values().stream().map(productoMapper::toModel).toList();
	}

	@Override
	public Producto crearNuevoProducto(Producto producto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Producto editarProducto(Producto producto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminaProducto(Producto producto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<Producto> obtenerProductoPorId(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Producto> filtrarProductos(String filtro) {
		// TODO Auto-generated method stub
		return null;
	}

}
