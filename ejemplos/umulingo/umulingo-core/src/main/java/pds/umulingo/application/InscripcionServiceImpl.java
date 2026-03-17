package pds.umulingo.application;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pds.umulingo.domain.model.curso.id.CursoId;
import pds.umulingo.domain.model.curso.model.Curso;
import pds.umulingo.domain.model.curso.repository.CursoRepository;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Inscripcion;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;
import pds.umulingo.domain.ports.input.InscripcionService;
import pds.umulingo.domain.ports.input.dto.InscripcionDTO;
import pds.umulingo.domain.ports.input.dto.InscripcionesUsuarioDTO;

@Service
public class InscripcionServiceImpl implements InscripcionService {

	private static final Logger log = LoggerFactory.getLogger(InscripcionServiceImpl.class);

	private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public InscripcionServiceImpl(CursoRepository cursoRepository,
                              UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public InscripcionDTO inscribirUsuario(String usuarioId, String cursoId) {
        UsuarioId usuarioIdVO = new UsuarioId(usuarioId);
        CursoId cursoIdVO = new CursoId(cursoId);

        Usuario u = usuarioRepository.findById(usuarioIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));
        
        Preconditions.checkNotNull(cursoRepository .findById(cursoIdVO));

        Inscripcion inscripcion = u.realizarInscripcion(cursoIdVO);
        usuarioRepository.save(u);
        return toDTO(inscripcion);
    }

    @Override
    public InscripcionesUsuarioDTO cursosInscrito(String usuarioId) {
    	UsuarioId usuarioIdVO = new UsuarioId(usuarioId);
    	Usuario u = usuarioRepository.findById(usuarioIdVO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

    	List<InscripcionesUsuarioDTO.CursoInscritoDTO> inscripcionesUsuario = new ArrayList<>();
    	for (Inscripcion inscripcion : u.getInscripciones()) {
    		log.debug("Inscripcion para curso {}", inscripcion.getCursoId());
			Curso curso = Preconditions.checkNotNull(cursoRepository.findById(inscripcion.getCursoId()));
			inscripcionesUsuario.add(new InscripcionesUsuarioDTO.CursoInscritoDTO(curso.getId(), curso.getNombre()));
		}
    	
    	return new InscripcionesUsuarioDTO(usuarioIdVO, inscripcionesUsuario);
	}
    
    private InscripcionDTO toDTO(Inscripcion inscripcion) {
        return new InscripcionDTO(
            inscripcion.getId().valor(),
            inscripcion.getUsuarioId().valor(),
            inscripcion.getCursoId().valor()
        );
    }


}
