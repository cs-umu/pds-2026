package umu.pds.calculoimpuestos.port;

public interface ObtencionIVA {
	enum TipoIVA {
		REDUCIDO, BASICO, GENERAL
	}

	double obtenerPara(TipoIVA tipo);
}
