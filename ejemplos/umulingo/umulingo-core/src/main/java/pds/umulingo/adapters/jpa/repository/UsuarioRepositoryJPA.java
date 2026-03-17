package pds.umulingo.adapters.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pds.umulingo.adapters.jpa.entity.UsuarioEntity;
import pds.umulingo.domain.model.usuario.id.UsuarioId;

@Repository
public interface UsuarioRepositoryJPA extends JpaRepository<UsuarioEntity, String> {

	Optional<UsuarioEntity> findByEmail(String value);

}
