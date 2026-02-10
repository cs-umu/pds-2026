package umu.pds.calculoimpuestos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import umu.pds.calculoIva.domain.model.Producto;
import umu.pds.calculoIva.domain.model.TipoProducto;
import umu.pds.calculoimpuestos.impl.CalculadorImpuestos;
import umu.pds.calculoimpuestos.impl.ObtencionIVAImpl;
import umu.pds.calculoimpuestos.port.ObtencionIVA;

public class CalculoIvaDesacoplado {

	private static final Logger log = LoggerFactory.getLogger(CalculoIvaDesacoplado.class);

	public static void main(String[] args) {
		// Obtengo el servicio que me dara los importes del iva
		// Este servicio puede ser interno en nuestra aplicacion o externo
		// Lo mapeo a una interfaz (port) por lo que la implementacion podra cambiar si
		// afectar a mi codigo
		ObtencionIVA servicioIva = new ObtencionIVAImpl();
		// El calculador de impuestos recibe el servicio como parametro
		CalculadorImpuestos calculador = new CalculadorImpuestos(servicioIva);

		Producto producto = leerProductoUI();
		double impuestos = calculador.calcular(producto.getPrecio(), producto.getTipoProducto());
		mostrarIVA(impuestos);

	}

	private static void mostrarIVA(double impuestos) {
		log.info("Impuestos: {}", impuestos);
	}

	// Metodo que simula la lectura de un producto del frontend1
	private static Producto leerProductoUI() {
		return new Producto(12.4, TipoProducto.ALIMENTACION);
	}

}
