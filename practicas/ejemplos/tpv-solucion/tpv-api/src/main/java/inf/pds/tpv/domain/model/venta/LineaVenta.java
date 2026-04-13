package inf.pds.tpv.domain.model.venta;

import java.util.Objects;
import java.util.UUID;

import inf.pds.tpv.adapters.memory.entity.ProductoMemory;
import inf.pds.tpv.domain.model.producto.Producto;

public class LineaVenta {

	private UUID identificador;
	private Producto producto;
	private int cantidad;

	public LineaVenta(Producto producto, int cantidad) {
		this.identificador= UUID.randomUUID();
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public double getTotal() {
		return this.producto.getPrecio()*cantidad;
	}
	
	public UUID getIdentificador() {
		return identificador;
	}

	public Producto getProducto() {
		return producto;
	}

	public int getCantidad() {
		return cantidad;
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
		if (!(obj instanceof LineaVenta)) {
			return false;
		}
		LineaVenta other = (LineaVenta) obj;
		return cantidad == other.cantidad && Objects.equals(identificador, other.identificador)
				&& Objects.equals(producto, other.producto);
	}

}
