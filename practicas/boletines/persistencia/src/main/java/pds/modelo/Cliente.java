package pds.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
    private String direccion;
    
    public Cliente(String nombreUsuario, String direccion) {
    	super(nombreUsuario);
    	this.direccion = direccion;
    }
    
    protected Cliente() { }
    
    public String getDireccion() {
		return direccion;
	}
    
    public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
}