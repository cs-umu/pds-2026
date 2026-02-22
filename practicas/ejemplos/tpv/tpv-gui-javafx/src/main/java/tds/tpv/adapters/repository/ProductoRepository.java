package tds.tpv.adapters.repository;

import java.util.List;

import tds.tpv.adapters.repository.exceptions.ProductoExistenteException;
import tds.tpv.adapters.repository.exceptions.ProductoNoEncontradoException;
import tds.tpv.adapters.repository.exceptions.ProductoPersistenciaException;
import tds.tpv.negocio.modelo.Producto;

public interface ProductoRepository {

	/**
	 * Devuelve una lista con todos los productos cargados
	 * 
	 * @return Lista de productos
	 */
	List<? extends Producto> getProductos();

	/**
	 * Busca un producto por su codigo y lo devuelve
	 * 
	 * @param codigo
	 * @throws ProductoNoEncontradoException si no encuentra el producto buscado
	 * @return Producto
	 */
	Producto findById(String codigo) throws ProductoNoEncontradoException;

	/**
	 * Aniade un producgto al inventario de productos
	 * 
	 * @param producto
	 * @throws ProductoExistenteException si existe un producto igual en el
	 *                                    repositorio
	 * @return void
	 */
	void addProducto(Producto producto) throws ProductoExistenteException,ProductoPersistenciaException;

	/**
	 * Elimina un producto del inventario de productos, si ya estaba eliminado no
	 * hace nada
	 * 
	 * @param producto
	 * @return void
	 */
	void removeProducto(Producto producto) throws ProductoPersistenciaException;

	/**
	 * Actualiza un producto en el almacen de productos, devolviendo la referencia al producto que
	 * se ha actualizado
	 * 
	 * @param producto
	 * @return Producto
	 */
	Producto updateProducto(Producto producto) throws ProductoPersistenciaException;

	List<? extends Producto> getProductosPorFiltro(String filtro);
}
