package pds.umulingo.domain.model.usuario.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

public class Usuario {
    private final UsuarioId id;
    private final String nombre;
    private final Email email;
    private final List<Inscripcion> inscripciones;
    
    public Usuario(Email email, String nombre, Collection<? extends Inscripcion> inscripciones) {
        this(email, nombre);
    	this.inscripciones.addAll(inscripciones);
    }
    
    public Usuario(Email email, String nombre) {
        this.id = new UsuarioId(email.value());
        this.nombre = nombre;
        this.email = email;
        this.inscripciones = new ArrayList<>();
    }

    public UsuarioId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Email getEmail() {
        return email;
    }
    
    public Inscripcion realizarInscripcion(CursoId curso) {
    	if (estaInscrito(curso)) {
    		throw new IllegalStateException("Usuario ya inscrito en curso");
    	}
    	
    	Inscripcion inscripcion = new Inscripcion(id, curso);
    	this.inscripciones.add(inscripcion);
    	// TODO: Emitir un evento
    	return inscripcion;
    }

	public boolean estaInscrito(CursoId curso) {
		for (Inscripcion inscripcion : inscripciones) {
			if (inscripcion.getCursoId().equals(curso)) {
				return true;
			}
		}
		return false;
	}

	public List<? extends Inscripcion> getInscripciones() {
		return this.inscripciones;
	}

}
