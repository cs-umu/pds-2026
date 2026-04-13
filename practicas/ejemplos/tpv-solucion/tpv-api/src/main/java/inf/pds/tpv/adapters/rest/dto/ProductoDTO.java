package inf.pds.tpv.adapters.rest.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductoDTO {
	private Long codigo;
	//Anotaciones de validacion
	@NotBlank(message = "Descripcion es obligatoria")
	private String descripcion;
	
	//Anotaciones de validacion
	@NotNull(message = "La cantidad es obligatoria")
	@Positive(message = "La cantidad debe ser mayor que 0")
	private Integer cantidad;
	
	//Anotaciones de validacion
	@NotNull(message = "El precio es obligatorio")
	@Positive(message = "El precio debe ser mayor que 0")
	private Double precio;

	public ProductoDTO(Long codigo, String descripcion, int cantidad, double precio) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
	}

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

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, codigo, descripcion, precio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ProductoDTO)) {
			return false;
		}
		ProductoDTO other = (ProductoDTO) obj;
		return cantidad == other.cantidad && Objects.equals(codigo, other.codigo)
				&& Objects.equals(descripcion, other.descripcion)
				&& Double.doubleToLongBits(precio) == Double.doubleToLongBits(other.precio);
	}

	@Override
	public String toString() {
		return "ProductoDTO [codigo=" + codigo + ", descripcion=" + descripcion + ", cantidad=" + cantidad + ", precio="
				+ precio + "]";
	}

}
