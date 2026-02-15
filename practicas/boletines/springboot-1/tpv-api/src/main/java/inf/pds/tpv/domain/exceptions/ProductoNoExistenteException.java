package inf.pds.tpv.domain.exceptions;

public class ProductoNoExistenteException extends Exception{

	public ProductoNoExistenteException(String mensaje) {
		super(mensaje);
	}
	
	public ProductoNoExistenteException(String mensaje, Exception e) {
		super(mensaje,e);
	}
	
}
