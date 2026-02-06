package pds.duolingo.domain.usuario;

public record Email(String value) {

	public static Email of(String value) {
		return new Email(value);
	}
	
}
