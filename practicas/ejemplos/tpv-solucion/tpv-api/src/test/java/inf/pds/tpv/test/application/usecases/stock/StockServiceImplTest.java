package inf.pds.tpv.test.application.usecases.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import inf.pds.tpv.application.usecases.stock.StockServiceImpl;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;
import inf.pds.tpv.domain.ports.input.stock.commands.CrearProductoCommand;
import inf.pds.tpv.domain.ports.output.ProductosRepository;

public class StockServiceImplTest {

    private StockServiceImpl servicio;
    private ProductosRepositoryEnMemoria repository;

    @BeforeEach
    void setUp() {
        repository = new ProductosRepositoryEnMemoria();
        servicio = new StockServiceImpl(repository);
    }

    @Test
    void testCrearProducto() {
        CrearProductoCommand c = new CrearProductoCommand("prueba", 10, 100.0);
        Producto p = servicio.crearNuevoProducto(c);
        
        assertEquals("prueba", p.getDescripcion());
        assertEquals(100.0, p.getPrecio());
        assertEquals(10, p.getCantidad());
     
        Producto encontrado = repository.obtenerProductoPorId(p.getIdentificador().getCodigoNumerico()).get();
        assertEquals("prueba", encontrado.getDescripcion());
        assertEquals(100.0, encontrado.getPrecio());
        assertEquals(10, encontrado.getCantidad());
    }
    
    private static class ProductosRepositoryEnMemoria implements ProductosRepository {
    	private Map<ProductoId, Producto> datos = new HashMap<>();
    	
		@Override
		public List<Producto> obtenerTodosProductos() {
			return new ArrayList<>(datos.values());
		}

		@Override
		public Producto crearNuevoProducto(Producto producto) {
			try {
				Producto nuevoProducto = new Producto(ProductoId.of(101 + datos.size() * 1L),
						producto.getDescripcion(), producto.getCantidad(), producto.getPrecio());
				datos.put(nuevoProducto.getIdentificador(), nuevoProducto);
				return nuevoProducto;
			} catch (IdentificadorProductoException e) {
				throw new IllegalStateException(e);
			}
		}

		@Override
		public Producto editarProducto(Producto producto) {
			datos.put(producto.getIdentificador(), producto);
			return producto;
		}

		@Override
		public void eliminaProducto(Producto producto) {
			datos.remove(producto.getIdentificador());
		}

		@Override
		public Optional<Producto> obtenerProductoPorId(Long id) {
			try {
				return Optional.ofNullable(datos.get(ProductoId.of(id)));
			} catch (IdentificadorProductoException e) {
				throw new IllegalStateException();
			}
		}

		@Override
		public List<Producto> filtrarProductos(String filtro) {
			throw new UnsupportedOperationException();
		}
    	
    }
}
