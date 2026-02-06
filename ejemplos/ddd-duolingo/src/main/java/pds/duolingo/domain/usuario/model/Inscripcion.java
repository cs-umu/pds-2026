package pds.duolingo.domain.usuario.model;

import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.usuario.id.InscripcionId;
import pds.duolingo.domain.usuario.id.UsuarioId;

public class Inscripcion {
    private final InscripcionId id;
    private final UsuarioId usuarioId;
    private final CursoId cursoId;

    public Inscripcion(InscripcionId id, UsuarioId usuarioId, CursoId cursoId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.cursoId = cursoId;
    }
    
    public Inscripcion(UsuarioId usuarioId, CursoId cursoId) {
    	this(InscripcionId.nuevo(), usuarioId, cursoId);
    }

    public InscripcionId getId() {
        return id;
    }

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }

    public CursoId getCursoId() {
        return cursoId;
    }
}
