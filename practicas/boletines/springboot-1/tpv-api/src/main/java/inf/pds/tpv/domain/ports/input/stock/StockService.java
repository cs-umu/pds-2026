package inf.pds.tpv.domain.ports.input.stock;

import java.util.List;

import inf.pds.tpv.domain.exceptions.ProductoNoExistenteException;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;
import inf.pds.tpv.domain.ports.input.stock.commands.CrearProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EditarProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EliminarProductoCommand;

public interface StockService {

	List<Producto> obtenerTodosProductos();

	Producto crearNuevoProducto(CrearProductoCommand cmd);

	Producto editarProducto(EditarProductoCommand cmd)
			throws ProductoNoExistenteException, IdentificadorProductoException;

	void eliminaProducto(EliminarProductoCommand cmd)
			throws ProductoNoExistenteException, IdentificadorProductoException;

}