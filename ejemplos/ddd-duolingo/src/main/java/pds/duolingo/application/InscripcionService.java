package pds.duolingo.application;

import pds.duolingo.application.dto.InscripcionDTO;
import pds.duolingo.domain.curso.id.CursoId;
import pds.duolingo.domain.curso.repository.CursoRepository;
import pds.duolingo.domain.usuario.id.UsuarioId;
import pds.duolingo.domain.usuario.model.Inscripcion;
import pds.duolingo.domain.usuario.model.Usuario;
import pds.duolingo.domain.usuario.repository.UsuarioRepository;

public class InscripcionService {
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public InscripcionService(CursoRepository cursoRepository,
                              UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public InscripcionDTO inscribirUsuario(String usuarioId, String cursoId) {
        UsuarioId usuarioIdVO = new UsuarioId(usuarioId);
        CursoId cursoIdVO = new CursoId(cursoId);

        Usuario u = usuarioRepository.findById(usuarioIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));
        cursoRepository.findById(cursoIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado: " + cursoId));

        Inscripcion inscripcion = u.realizarInscripcion(cursoIdVO);
        usuarioRepository.save(u);
        return toDTO(inscripcion);
    }

    private InscripcionDTO toDTO(Inscripcion inscripcion) {
        return new InscripcionDTO(
            inscripcion.getId().valor(),
            inscripcion.getUsuarioId().valor(),
            inscripcion.getCursoId().valor()
        );
    }
}
