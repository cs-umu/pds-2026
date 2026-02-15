package inf.pds.tpv.domain.ports.output;

import java.util.List;
import java.util.Optional;

import inf.pds.tpv.domain.model.producto.Producto;

public interface ProductosRepository {
	public List<Producto> obtenerTodosProductos();

	public Producto crearNuevoProducto(Producto producto);

	public Producto editarProducto(Producto producto);

	public void eliminaProducto(Producto producto);

	public Optional<Producto> obtenerProductoPorId(Long id);

}
