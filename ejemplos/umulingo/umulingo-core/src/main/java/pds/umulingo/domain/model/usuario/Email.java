package pds.umulingo.domain.model.usuario;

public record Email(String value) {

	public static Email of(String value) {
		return new Email(value);
	}
	
}
