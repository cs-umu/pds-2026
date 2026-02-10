package umu.pds.calculoiva.rest.adapters;

import umu.pds.calculoimpuestos.port.ObtencionIVA;

/**
 * Este adaptador implementa un IVA fijo
 */
public class SimpleIvaAdapter implements ObtencionIVA {

	@Override
	public double obtenerPara(TipoIVA tipo) {
		return switch (tipo) {
			case REDUCIDO -> 0.04;
			case BASICO -> 0.10;
			case GENERAL -> 0.21;
		};
	}

}
