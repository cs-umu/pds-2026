package inf.pds.tpv.application.usecases.stock;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import inf.pds.tpv.domain.exceptions.ProductoNoExistenteException;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;
import inf.pds.tpv.domain.ports.input.stock.StockService;
import inf.pds.tpv.domain.ports.input.stock.commands.CrearProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EditarProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EliminarProductoCommand;
import inf.pds.tpv.domain.ports.output.ProductosRepository;

@Service
public class StockServiceImpl implements StockService {

	private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

	private ProductosRepository productoPort;

	public StockServiceImpl(ProductosRepository productoPort) {
		this.productoPort = productoPort;
	}

	@Override
	public List<Producto> obtenerTodosProductos() {
		return this.productoPort.obtenerTodosProductos();
	}

	private Optional<Producto> obtenerProductoPorId(ProductoId id) {
		return this.productoPort.obtenerProductoPorId(id.getCodigoNumerico());
	}

	@Override
	public Producto crearNuevoProducto(CrearProductoCommand cmd) {
		// Creo un producto asignando un identificador nulo porque es un producto nuevo
		// y no tiene
		Producto nuevoProducto = new Producto(null, cmd.descripcion(), cmd.cantidad(), cmd.precio());
		// Pido al port que lo persista, tampoco necesito saber ni donde ni como
		nuevoProducto = this.productoPort.crearNuevoProducto(nuevoProducto);
		return nuevoProducto;
	}

	@Override
	public Producto editarProducto(EditarProductoCommand cmd)
			throws ProductoNoExistenteException, IdentificadorProductoException {

		ProductoId id = ProductoId.of(cmd.codigo());
		Optional<Producto> productoOptional = obtenerProductoPorId(id);
		// Si el producto no existe no puedo editarlo
		if (!productoOptional.isPresent()) {
			log.warn("El producto con id {} no se encuentra en la base de datos", cmd.codigo());
			throw new ProductoNoExistenteException(
					"El producto con id " + cmd.codigo() + " no se encuentra en la base de datos");
		} else {
			Producto producto = productoOptional.get();
			producto.setDescripcion(cmd.descripcion());
			producto.setCantidad(cmd.cantidad());
			producto.setPrecio(cmd.precio());
			return this.productoPort.editarProducto(producto);
		}

	}

	@Override
	public void eliminaProducto(EliminarProductoCommand cmd)
			throws ProductoNoExistenteException, IdentificadorProductoException {

		ProductoId id = ProductoId.of(cmd.codigo());
		Optional<Producto> producto = obtenerProductoPorId(id);
		// Si el producto no existe no puedo eliminarlo
		if (!producto.isPresent()) {
			log.warn("El producto con id {} no se encuentra en la base de datos", cmd.codigo());
			throw new ProductoNoExistenteException(
					"El producto con id " + cmd.codigo() + " no se encuentra en la base de datos");
		} else {

			this.productoPort.eliminaProducto(producto.get());
		}

	}

}
