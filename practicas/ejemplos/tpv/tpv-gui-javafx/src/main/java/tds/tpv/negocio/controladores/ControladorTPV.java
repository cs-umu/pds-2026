package tds.tpv.negocio.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;

import tds.tpv.adapters.repository.ProductoRepository;
import tds.tpv.adapters.repository.exceptions.ProductoExistenteException;
import tds.tpv.adapters.repository.exceptions.ProductoNoEncontradoException;
import tds.tpv.adapters.repository.exceptions.ProductoPersistenciaException;
import tds.tpv.negocio.modelo.CarritoCompra;
import tds.tpv.negocio.modelo.Item;
import tds.tpv.negocio.modelo.Producto;

public class ControladorTPV {

	private static final Logger log = LogManager.getLogger();

	private ProductoRepository repositorioProductos;
	private CarritoCompra carrito;

	public ControladorTPV(ProductoRepository rep) {
		this.repositorioProductos = rep;
		nuevoCarrito();
	}

	// Como podemos hacer para que la vista no pueda aniadir al carrito?
	// => Devolver una interfaz (o hacer que Carrito sea una interfaz y tener un
	// CarritoImpl para el controlador)
	public CarritoCompra nuevoCarrito() {
		this.carrito = new CarritoCompra();
		return carrito;
	}

	public List<? extends Producto> getProductos() {
		return repositorioProductos.getProductos();
	}

	public List<? extends Producto> getProductosPorFiltro(String filtro) {
		return repositorioProductos.getProductosPorFiltro(filtro);
	}

	/**
	 * Añade un producto al carrito.
	 * 
	 * @return El item añadido o modificado.
	 */
	public Item addToCarrito(Producto producto, int cantidad) {
		Preconditions.checkNotNull(carrito);
		log.info("Añadido producto {} al carrito con cantidad {}", producto.getCodigo(), cantidad);
		return this.carrito.addItem(producto, cantidad);
	}

	public void quitarFromCarrito(Producto producto) {
		Preconditions.checkNotNull(carrito);
		Preconditions.checkNotNull(producto);
		log.info("Quitamos producto ({}) {} del carrito ", producto.getCodigo(), producto.getDescripcion());
		this.carrito.removeItem(producto);
	}

	public CarritoCompra getCarrito() {
		Preconditions.checkNotNull(carrito);
		return carrito;
	}

	// La parte de stock podria estar en otro controlador

	/**
	 * Modifica la cantidad de stock. Si se quiere reducir la cantidad, se pasa un
	 * número negativo
	 * 
	 * @param producto El producto que se quiere modificar
	 * @param cantidad La cantidad que se suma (positivo) o resta (negativo) al
	 *                 stock actual
	 */
	private void modificarStock(Producto producto, int cantidad) {
		// Asegurar que el producto efectivamente existe
		Producto productoImpl;
		try {
			productoImpl = repositorioProductos.findById(producto.getCodigo());
			Preconditions.checkState(productoImpl == producto);

			// Asegurar que el stock nunca es negativo
			// Siempre sumamos porque el viewcontroller mandara un numero positivo o
			// negativo segun convenga
			Preconditions.checkState(productoImpl.getCantidad() + cantidad >= 0);

			productoImpl.setCantidad(productoImpl.getCantidad() + cantidad);
			repositorioProductos.updateProducto(productoImpl);
		} catch (ProductoNoEncontradoException e) {
			log.error("No se encuentra el producto {} para modifiarlo", producto.getCodigo(), e);

		} catch (ProductoPersistenciaException e) {
			log.error("Error en la actualizacion del producto {}", producto.getCodigo(), e);
		}
	}

	public void aumentarStock(Producto producto, int cantidad) {
		// Aseguramos que el valor de cantidad es siempre positivo
		Preconditions.checkState(cantidad >= 0);
		log.info("Aumentamos el stock del producto {} en {} unidades", producto.getCodigo(), cantidad);
		this.modificarStock(producto, cantidad);
	}

	public void reducirStock(Producto producto, int cantidad) {
		// Aseguramos que el valor de cantidad es siempre positivo
		Preconditions.checkState(cantidad >= 0);
		log.info("Reducimos el stock del producto {} en {} unidades", producto.getCodigo(), cantidad);
		this.modificarStock(producto, -cantidad);
	}

	public void nuevoProductoStock(Producto productoImpl) {
		try {
			repositorioProductos.addProducto(productoImpl);
			log.info("Creado nuevo producto ({}) {}", productoImpl.getCodigo(), productoImpl.getDescripcion());
		} catch (ProductoExistenteException e) {
			log.error("El producto ya existe {}", productoImpl.getCodigo(), e);

		} catch (ProductoPersistenciaException e) {
			log.error("Error en la persistencia del producto {}", productoImpl.getCodigo(), e);
		}

	}

	public void setProductoRepository(ProductoRepository repositorio) {
		this.repositorioProductos = repositorio;
	}

}
