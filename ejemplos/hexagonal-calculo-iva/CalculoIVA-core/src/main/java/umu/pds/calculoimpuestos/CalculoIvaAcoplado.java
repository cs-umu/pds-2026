package umu.pds.calculoimpuestos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import umu.pds.calculoIva.domain.model.Producto;
import umu.pds.calculoIva.domain.model.TipoProducto;

public class CalculoIvaAcoplado {
	private static final Logger log = LoggerFactory.getLogger(CalculoIvaAcoplado.class);

	public void main(String[] args) {
		Producto producto = leerProductoUI();
		double impuestos = calculadoraIVA(producto.getPrecio(), producto.getTipoProducto());
		mostrarIVA(impuestos);
	}

	// Dado el importe y el tipo de producto calculo el IVA asociado
	public static double calculadoraIVA(double precioSinIVA, TipoProducto tipoProducto) {
		return switch (tipoProducto) {
		// El IVA concreto esta en el codigo fuente, si cambia tengo que cambiar esta
		// implementacion
		case BASICO -> precioSinIVA * 0.04;
		case ALIMENTACION -> precioSinIVA * 0.10;
		case LUJO -> precioSinIVA * 0.21;
		};
	}

	/// METODOS AUXILIARES ///
	private static void mostrarIVA(double impuestos) {
		log.info("Impuestos: {}", impuestos);
	}

	// Metodo que simula la lectura de un producto del frontend1
	private static Producto leerProductoUI() {
		return new Producto(12.4, TipoProducto.ALIMENTACION);
	}

}
