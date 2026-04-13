package inf.pds.tpv.adapters.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//Raiz del agregado Venta - LineaVenta
public class VentaDTO {

	private UUID identificador;
	private List<LineaVentaDTO> lineasVenta;

	public VentaDTO() {
		this.identificador = UUID.randomUUID();
		lineasVenta = new ArrayList<LineaVentaDTO>();
	}
	
	public VentaDTO(UUID identificador) {
		this.identificador =identificador;
		lineasVenta = new ArrayList<LineaVentaDTO>();
	}

	@Override
	public int hashCode() {
		return Objects.hash(identificador, lineasVenta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VentaDTO)) {
			return false;
		}
		VentaDTO other = (VentaDTO) obj;
		return Objects.equals(identificador, other.identificador) && Objects.equals(lineasVenta, other.lineasVenta);
	}



	public UUID getIdentificador() {
		return identificador;
	}



	public void setIdentificador(UUID identificador) {
		this.identificador = identificador;
	}



	public List<LineaVentaDTO> getLineasVenta() {
		return lineasVenta;
	}



	public void setLineasVenta(List<LineaVentaDTO> lineasVenta) {
		this.lineasVenta = lineasVenta;
	}

}
