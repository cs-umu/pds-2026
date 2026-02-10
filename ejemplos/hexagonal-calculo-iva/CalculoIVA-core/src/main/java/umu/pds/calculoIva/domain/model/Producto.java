package umu.pds.calculoIva.domain.model;

public class Producto {

	private double precio;
	private TipoProducto tipoProducto;
	
	public Producto(double precio, TipoProducto tipoProducto) {

		this.precio = precio;
		this.tipoProducto = tipoProducto;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}



}
