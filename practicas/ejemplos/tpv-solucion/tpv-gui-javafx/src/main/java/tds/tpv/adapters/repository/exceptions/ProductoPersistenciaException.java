package tds.tpv.adapters.repository.exceptions;

public class ProductoPersistenciaException extends Exception {

	public ProductoPersistenciaException(String mensaje) {
		super(mensaje);
	}
	
	public ProductoPersistenciaException(Exception excepcion) {
		super(excepcion);
	}

}
