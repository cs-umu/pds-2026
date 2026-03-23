package pds.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario {
	private String permisos;
    
	public Admin(String nombreUsuario, String permisos) {
		super(nombreUsuario);
		this.permisos = permisos;
	}
	
	protected Admin() { }
	
	public String getPermisos() {
		return permisos;
	}
	
	public void setPermisos(String permisos) {
		this.permisos = permisos;
	}
    
}

