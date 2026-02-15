package inf.pds.tpv.adapters.jpa.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTO", schema = "TPV")
public class ProductoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@Column(name = "DESCRIPCION", nullable = false)
	private String descripcion;

	@Column(name = "CANTIDAD", nullable = false)
	private int cantidad;

	@Column(name = "PRECIO", nullable = false)
	private double precio;

	public ProductoEntity() {
	}

	public ProductoEntity(Long codigo, String descripcion, int cantidad, double precio) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	// Getters y setters

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ProductoEntity)) {
			return false;
		}

		ProductoEntity other = (ProductoEntity) o;
		return Objects.equals(codigo, other.codigo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}
}
