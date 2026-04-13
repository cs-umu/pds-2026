package inf.pds.tpv.adapters.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inf.pds.tpv.adapters.mappers.VentaMapper;
import inf.pds.tpv.adapters.rest.dto.VentaDTO;
import inf.pds.tpv.domain.model.venta.Venta;
import inf.pds.tpv.domain.ports.input.venta.VentaService;
import inf.pds.tpv.domain.ports.input.venta.command.CrearVentaCommand;
import inf.pds.tpv.domain.ports.input.venta.command.EditarVentaCommand;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${tpv.private.api}/ventas")
public class TpvEndpoint {

	private final VentaService ventaService;
	private VentaMapper ventaMapper;

	public TpvEndpoint(VentaService ventaService, VentaMapper ventaMapper) {
		this.ventaService = ventaService;
		this.ventaMapper = ventaMapper;
	}

	@PostMapping("/venta")
	public ResponseEntity<VentaDTO> crearVenta(@Valid @RequestBody VentaDTO venta) {
		Venta nuevaVenta = ventaService.crearVenta(new CrearVentaCommand(ventaMapper.toModel(venta)));
		return ResponseEntity.status(HttpStatus.OK).body(ventaMapper.toDTO(nuevaVenta));
	}

	@PutMapping("/venta")
	public ResponseEntity<VentaDTO> modificarVenta(@Valid @RequestBody VentaDTO venta) {
		Venta ventaEditada = ventaService.modificarVenta(new EditarVentaCommand(venta.getIdentificador(),ventaMapper.toModel(venta)));
		return ResponseEntity.status(HttpStatus.OK).body(ventaMapper.toDTO(ventaEditada));

	}

	@GetMapping("/venta")
	public ResponseEntity<List<VentaDTO>> getVentas() {
		List<VentaDTO> ventas = ventaService.obtenerTodasVentas().stream().map(venta -> ventaMapper.toDTO(venta)).toList();
		return  ResponseEntity.status(HttpStatus.OK).body(ventas);
	}

	@GetMapping("/venta/{id}")
	public ResponseEntity<VentaDTO> getVenta(@PathVariable String id) {		
		Venta venta = ventaService.obtenerVenta(UUID.fromString(id));
		if(venta != null) {
			return ResponseEntity.status(HttpStatus.OK).body(ventaMapper.toDTO(venta));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}