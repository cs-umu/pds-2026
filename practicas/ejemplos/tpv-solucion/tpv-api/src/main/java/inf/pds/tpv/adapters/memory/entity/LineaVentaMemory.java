package inf.pds.tpv.adapters.memory.entity;

import java.util.Objects;
import java.util.UUID;

public class LineaVentaMemory {

	private UUID identificador;
	private ProductoMemory producto;
	private int cantidad;

	public LineaVentaMemory(ProductoMemory producto, int cantidad) {
		this.identificador= UUID.randomUUID();
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public UUID getIdentificador() {
		return identificador;
	}

	public void setIdentificador(UUID identificador) {
		this.identificador = identificador;
	}

	public ProductoMemory getProducto() {
		return producto;
	}

	public void setProducto(ProductoMemory producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, identificador, producto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LineaVentaMemory)) {
			return false;
		}
		LineaVentaMemory other = (LineaVentaMemory) obj;
		return cantidad == other.cantidad && Objects.equals(identificador, other.identificador)
				&& Objects.equals(producto, other.producto);
	}

}
