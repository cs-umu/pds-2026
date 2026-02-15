package inf.pds.tpv.adapters.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inf.pds.tpv.adapters.mappers.ProductoMapper;
import inf.pds.tpv.adapters.rest.dto.ProductoDTO;
import inf.pds.tpv.domain.exceptions.ProductoNoExistenteException;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;
import inf.pds.tpv.domain.ports.input.stock.StockService;
import inf.pds.tpv.domain.ports.input.stock.commands.CrearProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EditarProductoCommand;
import inf.pds.tpv.domain.ports.input.stock.commands.EliminarProductoCommand;

//Indico que es un controlador rest
@RestController
//Construyo la ruta a partir de la base en (application.properties) y el
// sufijo del endpoint
@RequestMapping("${tpv.private.api}/stock")
public class StockEndpoint {

	private static final Logger log = LoggerFactory.getLogger(StockEndpoint.class);

	private StockService controladorStock;
	private ProductoMapper productoMapper;

	public StockEndpoint(StockService controladorStock, ProductoMapper productoMapper) {
		this.controladorStock = controladorStock;
		this.productoMapper = productoMapper;
	}

	// La ruta de acceso seria:
	// http://localhost:8080/tpv/private/v1.0/stock/producto
	@GetMapping("/producto")
	public ResponseEntity<List<ProductoDTO>> getProductos() {
		log.info("Listando todos los productos");
		List<Producto> productos = controladorStock.obtenerTodosProductos();
		return ResponseEntity.status(HttpStatus.OK).body(productos.stream().map(productoMapper::toDTO).toList());
	}

	// Peticion POST que se debe hacer mandando en el cuerpo un JSON
	// http://localhost:8080/tpv/private/v1.0/stock/producto
	// @RequestBody Indica que el contenido viene en el cuerpo de la peticion
	@PostMapping("/producto")
	public ResponseEntity<ProductoDTO> createProducto(@RequestBody ProductoDTO productoDto) {
		log.info("Creando nuevo producto {}", productoDto.getDescripcion());
		// Si el ProductoDTO tiene codigo no puedo persistirlo
		// Respondo diciendo que es uan peticion erronea
		if (productoDto.getCodigo() != null) {
			log.warn("Se intenta persistir un producto que ya tiene identificador {}", productoDto);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		CrearProductoCommand cmd = new CrearProductoCommand(productoDto.getDescripcion(),
				productoDto.getCantidad(), productoDto.getPrecio());
		Producto nuevoProducto = controladorStock.crearNuevoProducto(cmd);

		return ResponseEntity.status(HttpStatus.OK).body(productoMapper.toDTO(nuevoProducto));
	}

	// Peticion PUT que modifica un producto existente
	// http://localhost:8080/tpv/private/v1.0/stock/producto
	// @RequestBody Indica que el contenido viene en el cuerpo de la peticion
	@PutMapping("/producto")
	public ResponseEntity<ProductoDTO> modificaProducto(@RequestBody ProductoDTO productoDto) {
		log.info("Editando producto {}", productoDto.getCodigo());
		try {
			EditarProductoCommand cmd = new EditarProductoCommand(productoDto.getCodigo(),
					productoDto.getDescripcion(), productoDto.getCantidad(), productoDto.getPrecio());
			Producto producto = this.controladorStock.editarProducto(cmd);
			return ResponseEntity.status(HttpStatus.OK).body(productoMapper.toDTO(producto));
		} catch (ProductoNoExistenteException | IdentificadorProductoException ex) {
			// Si el producto no existe o el id es invalido, devuelvo codigo 400 -> Bad Request
			log.warn("El producto con id {} no se encuentra en la base de datos", productoDto.getCodigo());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping("/producto/{producto}")
	public ResponseEntity<ProductoDTO> eliminaProducto(@PathVariable long producto) {
		try {
			EliminarProductoCommand cmd = new EliminarProductoCommand(producto);
			this.controladorStock.eliminaProducto(cmd);
			log.info("Producto {} eliminado", producto);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (ProductoNoExistenteException | IdentificadorProductoException ex) {
			// Si el producto no existe o el id es invalido, devuelvo codigo 400 -> Bad Request
			log.warn("El producto con id {} no se encuentra en la base de datos", producto);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

}
