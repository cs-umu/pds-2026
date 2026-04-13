package inf.pds.tpv.domain.model.venta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import inf.pds.tpv.domain.model.producto.Producto;

//Raiz del agregado Venta - LineaVenta
public class Venta {

	private UUID identificador;
	private List<LineaVenta> lineasVenta;

	public Venta() {
		this.identificador = UUID.randomUUID();
		lineasVenta = new ArrayList<>();
	}
	
	public Venta(UUID identificador) {
		this.identificador = identificador;
		lineasVenta = new ArrayList<>();
	}

	public void aniadeLineaVenta(Producto producto, int cantidad) {
		LineaVenta nuevaVenta = new LineaVenta(producto, cantidad);
		lineasVenta.add(nuevaVenta);
	}
	
	public void eliminaLineaVenta(Producto producto, int cantidad) {
		//Importante: Cuando elimino una linea de venta no la borro, genero una igual pero con valor negativo
		//Fijate en los tickets cuando compras, nunca se borran las lineas de venta
		LineaVenta nuevaVenta = new LineaVenta(producto, -cantidad);
		lineasVenta.add(nuevaVenta);
	}
	
	public double getTotal() {
		//Recorro las lineas de venta, paso el contenido a un double que representa el total y lo sumo 
		return lineasVenta.stream().mapToDouble(LineaVenta::getTotal).sum();
	}
	
	
	//Importante: No ponemos ni get ni set de las lineas de venta ya que si no romperiamos la raiz del agregado
	public UUID getIdentificador() {
		return identificador;
	}
	
	//Devuelvo las lineas de venta como una lista no modificable
	//Como las lineas de venta no tienen metodo set tampoco pueden ser modificadas
	//Si tuvieran metodo set deberia clonarlas para garantizar que solo
	//la raiz del agregado modifica la informacion
	public List<LineaVenta> getLineasVenta() {
        return Collections.unmodifiableList(lineasVenta);
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
		if (!(obj instanceof Venta)) {
			return false;
		}
		Venta other = (Venta) obj;
		return Objects.equals(identificador, other.identificador) && Objects.equals(lineasVenta, other.lineasVenta);
	}

	

}
