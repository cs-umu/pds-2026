package tds.tpv.adapters.repository.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tds.tpv.adapters.repository.ProductoRepository;
import tds.tpv.adapters.repository.exceptions.ProductoExistenteException;
import tds.tpv.adapters.repository.exceptions.ProductoNoEncontradoException;
import tds.tpv.adapters.repository.exceptions.ProductoPersistenciaException;
import tds.tpv.negocio.modelo.Producto;

/**
 * Esta clase gestionara el acceso al stock via api REST
 */
public class ProductoRestRepositoryImpl implements ProductoRepository {

	private static final Logger log = LoggerFactory.getLogger(ProductoRestRepositoryImpl.class);

	private static final String PRODUCTO_ENDPOINT = "http://localhost:8080/tpv/private/v1.0/stock/producto";

	private HttpClient client = HttpClient.newHttpClient();

	@Override
	public List<? extends Producto> getProductos() {
		List<Producto> productos = new ArrayList<Producto>();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(PRODUCTO_ENDPOINT)).GET().build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				productos = mapper.readValue(response.body(), new TypeReference<List<Producto>>() {	});
				log.info("Recuperados {} productos",productos.size());
			}
		} catch (IOException e) {
			log.error("Error recuperando productos", e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("Error recuperando productos", e);
		}

		return productos;
	}
	
	@Override
	public List<? extends Producto> getProductosPorFiltro(String filtro) {
		//Si no tiene filtro llamo al obtener todos los productos
		if(filtro==null || filtro.length()==0) {
			return getProductos();
		}
		
		List<Producto> productos = new ArrayList<Producto>();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(PRODUCTO_ENDPOINT+"/"+filtro)).GET().build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				productos = mapper.readValue(response.body(), new TypeReference<List<Producto>>() {	});
				log.info("Recuperados {} productos",productos.size());
			}
		} catch (IOException e) {
			log.error("Error recuperando productos", e);
			e.printStackTrace();
		} catch (InterruptedException e) {
			log.error("Error recuperando productos", e);
		}

		return productos;
	}

	@Override
	public Producto findById(String codigo) throws ProductoNoEncontradoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProducto(Producto producto) throws ProductoExistenteException, ProductoPersistenciaException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeProducto(Producto producto) throws ProductoPersistenciaException {
		// TODO Auto-generated method stub

	}

	@Override
	public Producto updateProducto(Producto producto) throws ProductoPersistenciaException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
