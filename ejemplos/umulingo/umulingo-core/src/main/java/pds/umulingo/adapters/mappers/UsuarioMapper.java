package pds.umulingo.adapters.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import pds.umulingo.adapters.jpa.entity.InscripcionEntity;
import pds.umulingo.adapters.jpa.entity.UsuarioEntity;
import pds.umulingo.domain.model.usuario.model.Inscripcion;
import pds.umulingo.domain.model.usuario.model.Usuario;

@Component
public class UsuarioMapper {
	
	public UsuarioEntity toEntity(Usuario usuario) {
		List<InscripcionEntity> inscripciones = usuario.getInscripciones().stream().map(i -> {
			return new InscripcionEntity(i.getId(), i.getUsuarioId(), i.getCursoId());
		}).toList();
		
		return new UsuarioEntity(usuario.getId(), usuario.getEmail(), usuario.getNombre(), inscripciones);
	}
	

	public Usuario toModel(UsuarioEntity entity) {
		List<Inscripcion> inscripciones = entity.getInscripciones().stream().map(i -> {
			return new Inscripcion(i.getId(), i.getUsuarioId(), i.getCursoId());
		}).toList();
		return new Usuario(entity.getEmail(), entity.getNombre(), inscripciones);
	}

}
