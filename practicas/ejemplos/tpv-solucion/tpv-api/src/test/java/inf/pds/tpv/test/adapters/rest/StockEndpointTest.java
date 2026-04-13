package inf.pds.tpv.test.adapters.rest;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import inf.pds.tpv.adapters.rest.dto.ProductoDTO;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StockEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE = "/tpv/private/v1.0/stock";

    // Todos los campos válidos y sin código 200, producto con código >= 131
    @Test
    void createProducto_datosValidos_devuelve200YProductoConCodigo() throws Exception {
        String json = """
                {
                    "descripcion": "Papaya Nueva",
                    "cantidad": 15,
                    "precio": 3.00
                }
                """;

        MvcResult result = mockMvc.perform(post(BASE + "/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();

        ProductoDTO producto = objectMapper.readValue(responseJson, ProductoDTO.class);

        assertNotNull(producto);
        assertTrue(producto.getCodigo() >= 131);
        assertEquals("Papaya Nueva", producto.getDescripcion());
        assertEquals(15, producto.getCantidad());
        assertEquals(3.00, producto.getPrecio());
    }

    // Todos los campos válidos y sin código 200, producto con código >= 131
    @Test
    void createProducto_datosValidos_devuelve200YProductoConCodigo_usandoJsonPath() throws Exception {
        String json = """
                {
                    "descripcion": "Papaya Nueva",
                    "cantidad": 15,
                    "precio": 3.00
                }
                """;

        mockMvc.perform(post(BASE + "/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(greaterThanOrEqualTo(131)))
                .andExpect(jsonPath("$.descripcion").value("Papaya Nueva"))
                .andExpect(jsonPath("$.cantidad").value(15))
                .andExpect(jsonPath("$.precio").value(3.00));
    }
    
    @Test
    void createProducto_cantidadCero_devuelve400() throws Exception {
        String json = """
                {
                    "descripcion": "Test",
                    "cantidad": 0,
                    "precio": 1.20
                }
                """;

        mockMvc.perform(post(BASE + "/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}