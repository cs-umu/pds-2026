package inf.pds.tpv.adapters.rest.dto;

import java.util.Objects;
import java.util.UUID;

public class LineaVentaDTO {

	private UUID identificador;
	private ProductoDTO producto;
	private int cantidad;

	public LineaVentaDTO(ProductoDTO producto, int cantidad) {
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

	public ProductoDTO getProducto() {
		return producto;
	}

	public void setProducto(ProductoDTO producto) {
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
		if (!(obj instanceof LineaVentaDTO)) {
			return false;
		}
		LineaVentaDTO other = (LineaVentaDTO) obj;
		return cantidad == other.cantidad && Objects.equals(identificador, other.identificador)
				&& Objects.equals(producto, other.producto);
	}

}
