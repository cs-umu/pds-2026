package umu.pds.calculoimpuestos.impl;

import umu.pds.calculoIva.domain.model.TipoProducto;
import umu.pds.calculoimpuestos.port.ObtencionIVA;

public class CalculadorImpuestos {
	private ObtencionIVA iva;

	public CalculadorImpuestos(ObtencionIVA iva) {
		this.iva = iva;
	}

	// Calculo el IVA a aplicar segun el tipo de producto sin la necesidad de sabe
	// el valor exacto
	// El servicio proveera el valor acorde
	public double calcular(double precioSinIVA, TipoProducto tipoProducto) {
		ObtencionIVA.TipoIVA ivaAAplicar = switch (tipoProducto) {
		case BASICO -> ObtencionIVA.TipoIVA.REDUCIDO;
		case ALIMENTACION -> ObtencionIVA.TipoIVA.BASICO;
		case LUJO -> ObtencionIVA.TipoIVA.GENERAL;
		};
		// El servicio provee el metodo obtenerPara, por lo que tampoco necesito conocer
		// ese calculo
		return iva.obtenerPara(ivaAAplicar) * precioSinIVA;
	}

}
