package pds.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// =======================
//    ENTITY: Category (N:N Book)
// =======================
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    
    public Categoria(String nombre) {
    	this.nombre = nombre;
    }
    
    protected Categoria() { }
    
    public String getNombre() {
		return nombre;
	}
    
    public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}