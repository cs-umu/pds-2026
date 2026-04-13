package inf.pds.tpv.test.application.usecases.stock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import inf.pds.tpv.application.usecases.stock.StockServiceImpl;
import inf.pds.tpv.domain.model.producto.Producto;
import inf.pds.tpv.domain.model.producto.ProductoId;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;
import inf.pds.tpv.domain.ports.input.stock.commands.CrearProductoCommand;
import inf.pds.tpv.domain.ports.output.ProductosRepository;

@ExtendWith(MockitoExtension.class)
public class StockServiceImplTestMockito {

    @Mock
    private ProductosRepository repository;

    @InjectMocks
    private StockServiceImpl servicio;

    @Test
    void testCrearProducto() throws IdentificadorProductoException {
        CrearProductoCommand c = new CrearProductoCommand("prueba", 10, 100.0);

        Producto productoGuardado = new Producto(
                ProductoId.of(101L),
                "prueba",
                10,
                100.0
        );

        // Cuando se llame al método crearNuevoProducto(...) del mock 'repository'
        // con cualquier objeto de tipo Producto (no importa cuál sea),
        // entonces devuelva el objeto 'productoGuardado'.
        when(repository.crearNuevoProducto(any(Producto.class)))
                .thenReturn(productoGuardado);

        // Invocar al método que se quiere probar
        Producto p = servicio.crearNuevoProducto(c);

        // Comprobar si es correcto
        assertEquals("prueba", p.getDescripcion());
        assertEquals(100.0, p.getPrecio());
        assertEquals(10, p.getCantidad());

        // Verificamos que el método crearNuevoProducto(...) del mock
        // haya sido invocado exactamente una vez durante la ejecución del test.
        verify(repository, times(1)).crearNuevoProducto(any(Producto.class));
    }
}