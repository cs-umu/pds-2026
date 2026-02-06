package pds.duolingo.domain.usuario.model;

import java.util.ArrayList;
import java.util.List;

import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.usuario.Email;
import pds.duolingo.domain.usuario.id.UsuarioId;

public class Usuario {
    private final UsuarioId id;
    private final String nombre;
    private final Email email;
    private final List<Inscripcion> inscripciones;
    
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

	private boolean estaInscrito(CursoId curso) {
		return inscripciones.stream().anyMatch(i -> i.getCursoId() == curso);
	}

}
