package pds.umulingo.adapters.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import pds.umulingo.adapters.jpa.entity.UsuarioEntity;
import pds.umulingo.adapters.jpa.repository.UsuarioRepositoryJPA;
import pds.umulingo.adapters.mappers.UsuarioMapper;
import pds.umulingo.domain.model.usuario.Email;
import pds.umulingo.domain.model.usuario.id.UsuarioId;
import pds.umulingo.domain.model.usuario.model.Usuario;
import pds.umulingo.domain.model.usuario.repository.UsuarioRepository;

@Component
public class UsuarioRepositoryImpl implements UsuarioRepository {

	private UsuarioRepositoryJPA jpa;
	private UsuarioMapper mapper;

	public UsuarioRepositoryImpl(UsuarioRepositoryJPA jpa, UsuarioMapper mapper) {
		this.jpa = jpa;
		this.mapper = mapper;
	}
	
	@Override
	public void save(Usuario usuario) {
		jpa.save(mapper.toEntity(usuario));
	}

	@Override
	public Optional<Usuario> findById(UsuarioId id) {
		Optional<UsuarioEntity> usuario = jpa.findById(id.valor());
		return usuario.map(mapper::toModel);
	}

	@Override
	public Optional<Usuario> buscarPorEmail(Email email) {
		return jpa.findByEmail(email.value()).map(mapper::toModel);
	}

	@Override
	public List<Usuario> buscarTodos() {
		return jpa.findAll().stream().map(mapper::toModel).toList();
	}

	@Override
	public void eliminar(UsuarioId id) {
		jpa.deleteById(id.valor());
	}

}
