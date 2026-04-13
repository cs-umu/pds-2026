package inf.pds.tpv.domain.model.producto;

import java.util.Objects;

//El identificador de producto se compone de un codigo de 4 digitos y dos letras: 0001AB
//Este Value Object no se ha modelado como record ya que hemos creado dos metodos diferentes para su creacion
//que reciben parametros diferentes
public class ProductoId {

	private Long codigoNumerico;

	public static class IdentificadorProductoException extends Exception {
		private static final long serialVersionUID = 8739662863787515391L;

		public IdentificadorProductoException(String mensaje) {
			super(mensaje);
		}

		public IdentificadorProductoException(String mensaje, Exception ex) {
			super(mensaje, ex);
		}
	}

	private ProductoId(Long codigoNumerico) {
		this.codigoNumerico = codigoNumerico;
	}

	// Para ilustrar una regla de validación asumimos que en este negocio los primeros 100 códigos
	// de producto están reservados.
	public static ProductoId of(Long codigoNumerico) throws IdentificadorProductoException {
		if (codigoNumerico == null || (codigoNumerico <= 100)) {
			throw new IdentificadorProductoException("El codigoNumerico tiene que ser mayor que 100");
		}
		return new ProductoId(codigoNumerico);
	}


	public Long getCodigoNumerico() {
		return codigoNumerico;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoNumerico);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ProductoId)) {
			return false;
		}
		ProductoId other = (ProductoId) obj;
		return codigoNumerico == other.codigoNumerico;
	}

}
