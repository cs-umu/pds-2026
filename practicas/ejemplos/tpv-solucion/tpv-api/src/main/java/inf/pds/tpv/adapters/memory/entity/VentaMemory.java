package inf.pds.tpv.adapters.memory.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//Raiz del agregado Venta - LineaVenta
public class VentaMemory {

	private UUID identificador;
	private List<LineaVentaMemory> lineasVenta;

	public VentaMemory() {
		this.identificador = UUID.randomUUID();
		lineasVenta = new ArrayList<LineaVentaMemory>();
	}
	
	public VentaMemory(UUID identificador) {
		this.identificador = identificador;
		lineasVenta = new ArrayList<LineaVentaMemory>();
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
		if (!(obj instanceof VentaMemory)) {
			return false;
		}
		VentaMemory other = (VentaMemory) obj;
		return Objects.equals(identificador, other.identificador) && Objects.equals(lineasVenta, other.lineasVenta);
	}



	public UUID getIdentificador() {
		return identificador;
	}



	public void setIdentificador(UUID identificador) {
		this.identificador = identificador;
	}



	public List<LineaVentaMemory> getLineasVenta() {
		return lineasVenta;
	}



	public void setLineasVenta(List<LineaVentaMemory> lineasVenta) {
		this.lineasVenta = lineasVenta;
	}

}
