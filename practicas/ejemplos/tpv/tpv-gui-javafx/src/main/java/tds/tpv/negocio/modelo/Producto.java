package tds.tpv.negocio.modelo;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

//Tiene que implementar serializable para poder hacer Drag&Drop
public class Producto implements Serializable{
		
	@JsonProperty("codigo")
	private String codigo;
	@JsonProperty("descripcion")
	private String descripcion;
	@JsonProperty("cantidad")
	private int cantidad;
	@JsonProperty("precio")
	private double precio;

	public Producto() {
		this(null, null, 0, 0);		
	}
	
	public Producto(String codigo, String descripcion, int cantidad, double precio) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	/**
	 * Devuelve el codigo del producto
	 * @return String
	 */
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * Devuelve la descripcion del producto
	 * @return String
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Devuelve la cantidad de producto que queda sin vender
	 * @return int
	 */
	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Devuelve el precio de una unidad de producto
	 * @return int
	 */
	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, codigo, descripcion, precio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return codigo.equals(other.getCodigo());
	}

}
