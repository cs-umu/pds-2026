package tds.tpv.adapters.repository.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tds.tpv.Configuracion;
import tds.tpv.adapters.repository.ProductoRepository;
import tds.tpv.adapters.repository.exceptions.ProductoExistenteException;
import tds.tpv.adapters.repository.exceptions.ProductoPersistenciaException;
import tds.tpv.negocio.modelo.Producto;

public class ProductoRepositoryJSONImpl implements ProductoRepository {
	private static final Logger log = LogManager.getLogger();

	private List<Producto> productos = null;
	private String rutaFichero;

	private void cargaProductos() throws ProductoPersistenciaException {
		try {
			rutaFichero = Configuracion.getInstancia().getRutaAlmacen();
			this.productos = cargarProductos(rutaFichero);
		} catch (Exception e) {
			log.error("Error cargando los productos ", e);
			throw new ProductoPersistenciaException(e);
		}
	}

	@Override
	public List<? extends Producto> getProductos() {
		if (productos == null) {
			try {
				cargaProductos();
			} catch (ProductoPersistenciaException e) {
				// Manejo la excepcion pero no la propago porque en este sitio es donde se puede
				// gestionar mejor
				log.error("No se han podido cargar los productos ", e);
			}
		}
		return productos;
	}

	@Override
	public Producto findById(String codigo) {
		return productos.stream().filter(producto -> producto.getCodigo().equals(codigo)).findFirst().orElse(null);
	}

	@Override
	public void addProducto(Producto producto) throws ProductoExistenteException, ProductoPersistenciaException {
		// Si el producto ya existe no puedo insertarlo
		if (productos.contains(producto)) {
			throw new ProductoExistenteException("El producto " + producto.getCodigo() + " ya existe en el inventario");
		}
		productos.add(producto);
		try {
			guardarProductos(productos, rutaFichero);
		} catch (Exception e) {
			log.error("Error persistiendo el producto {}", producto.getCodigo(), e);
			// Capturo las excepciones genericas lanzadas al persistir y lanzo una propia
			// encapsulando la excepcion real
			throw new ProductoPersistenciaException(e);
		}

	}

	@Override
	public void removeProducto(Producto producto) throws ProductoPersistenciaException {

		if (!productos.contains(producto)) {
			return;
		}

		productos.remove(producto);
		try {
			guardarProductos(productos, rutaFichero);
		} catch (Exception e) {
			log.error("Error eliminando el producto {}", producto.getCodigo(), e);
			throw new ProductoPersistenciaException(e);
		}
	}

	@Override
	public Producto updateProducto(Producto producto) throws ProductoPersistenciaException {
		// No borramos los productos con stock 0
		try {
			guardarProductos(productos, rutaFichero);
			return producto;
		} catch (Exception e) {
			log.error("Error eliminando el producto {}", producto.getCodigo(), e);
			throw new ProductoPersistenciaException(e);
		}
	}

	// Metodo para cargar los productos incialmente desde fichero json
	private List<Producto> cargarProductos(String rutaFichero)
			throws StreamReadException, DatabindException, IOException {

		InputStream ficheroStream = getClass().getResourceAsStream(rutaFichero);

		ObjectMapper mapper = new ObjectMapper();
		List<Producto> productos = mapper.readValue(ficheroStream, new TypeReference<List<Producto>>() {
		});

		return productos;
	}

	// Metodo para guardar por completo en el fichero json
	private void guardarProductos(List<Producto> productos, String rutaFichero)
			throws StreamWriteException, DatabindException, IOException {

		// Se carga mediante URL para prevenir problemas con rutas con espacios en
		// blanco o caracteres no estandar
		URL url = getClass().getResource(rutaFichero);
		try {
			// Cargo el fichero a partir de la URL local
			File ficheroJSon = Paths.get(url.toURI()).toFile();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(ficheroJSon, productos);
		} catch (IOException | URISyntaxException e) {
			log.error("Error persistiendo en fichero", e);
		}
	}

	@Override
	public List<? extends Producto> getProductosPorFiltro(String filtro) {
		if (productos == null) {
			try {
				cargaProductos();
			} catch (ProductoPersistenciaException e) {
				// Manejo la excepcion pero no la propago porque en este sitio es donde se puede
				// gestionar mejor
				log.error("No se han podido cargar los productos ", e);
			}
		}
		return productos.stream()
				.filter(producto -> producto.getDescripcion().toLowerCase().contains(filtro.toLowerCase()))
				.collect(Collectors.toList());
	}

}
