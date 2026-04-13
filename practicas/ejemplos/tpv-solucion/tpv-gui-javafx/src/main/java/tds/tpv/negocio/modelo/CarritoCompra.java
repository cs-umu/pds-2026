package tds.tpv.negocio.modelo;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {

	private List<Item> items = new ArrayList<>();
	
	/**
	 * Devuelve una lista con los items par (producto, cantidad) que hay en el carrito
	 * @return Lista de Item
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * Añade un item carrito comprobando que si ya hay un item para ese producto
	 * se incrementa su cantidad en lugar de añadir otro.
	 */
	public Item addItem(Producto producto, int cantidad) {
		for (Item item : items) {
			if (item.getProducto().equals(producto)) {
				item.incrementarCantidad(cantidad);
				return item;
			}
		}
		Item item = new Item(producto, cantidad);
		this.items.add(item);
		return item;
	}
	
	public void removeItem(Producto producto) {
		Item itemSeleccionado=null;
		for (Item item : items) {
			if (item.getProducto() == producto) {
				itemSeleccionado = item;
				break;
			}
		}
		if (itemSeleccionado != null) {
			this.items.remove(itemSeleccionado);
		}
	}

	/**
	 * Calcula el importe total del carrito en base a los items que contiene
	 * @return double
	 */
	public double getTotal() {
		return getItems().stream().mapToDouble(item -> item.getProducto().getPrecio()*item.getCantidad()).sum();
	}	
	
}
