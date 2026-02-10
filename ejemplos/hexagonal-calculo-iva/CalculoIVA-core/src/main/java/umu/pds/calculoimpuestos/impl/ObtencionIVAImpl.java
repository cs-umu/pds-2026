package umu.pds.calculoimpuestos.impl;

import umu.pds.calculoimpuestos.port.ObtencionIVA;

public class ObtencionIVAImpl implements ObtencionIVA {

	public double obtenerPara(TipoIVA tipoIVA) {
		return switch (tipoIVA) {
		case REDUCIDO -> 0.04;
		case BASICO -> 0.10;
		case GENERAL -> 0.21;
		};
	}
}
